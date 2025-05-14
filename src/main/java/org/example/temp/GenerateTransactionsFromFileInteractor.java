package org.example.temp;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryTransactionFromFile;
import org.example.usecase.GenerateTransactionsFromFileUseCase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GenerateTransactionsFromFileInteractor implements GenerateTransactionsFromFileUseCase {
    @Override
    public Single<List<BoundaryTransactionFromFile>> execute(byte[] fileBytes) {
        return Single.fromCallable(() -> {
            List<String> extractedTexts = extractTextFromImage(fileBytes);
            List<BoundaryTransactionFromFile> transactions = new ArrayList<>();

            List<String> titleBuffer = new ArrayList<>();
            List<Float> amountBuffer = new ArrayList<>();
            Pattern datePattern = Pattern.compile(
                    "\\b(\\d{2}[./-]\\d{2}[./-]\\d{4}|\\d{4}[./-]\\d{2}[./-]\\d{2})\\b");
            String currentDate = null;

            for (String line : extractedTexts) {
                line = line.trim();

                Matcher dateMatcher = datePattern.matcher(line);
                if (dateMatcher.find()) {
                    String rawDate = dateMatcher.group();

                    // Try parsing with known formats
                    currentDate = tryParseAndFormatDate(rawDate);
                    continue;
                }

                if (line.matches(".*[\\d][.,][\\d].*")) {
                    // It's a float
                    String cleaned = line.replace(",", ".").replaceAll("[^\\d.\\-]", "");
                    try {
                        float amount = Float.parseFloat(cleaned);
                        amountBuffer.add(amount);
                    } catch (NumberFormatException e) {
                        // not a valid float, treat as title
                        titleBuffer.add(line);
                    }
                } else {
                    // It's a title or other text
                    // If we previously collected amounts, time to flush the buffer
                    if (!amountBuffer.isEmpty()) {
                        int titleSize = titleBuffer.size();
                        int amountSize = amountBuffer.size();

                        for (int i = 0; i < amountSize; i++) {
                            String title = "";

                            // Look back from the end of titleBuffer for a title with letters
                            for (int j = titleBuffer.size() - 1 - i; j >= 0; j--) {
                                String candidate = titleBuffer.get(j).trim();
                                if (candidate.matches(".*[a-zA-Z].*")) {
                                    title = candidate;
                                    break;
                                }
                            }

                            transactions.add(new BoundaryTransactionFromFile(
                                    Optional.of(amountBuffer.get(i)),
                                    Optional.of(title),
                                    Optional.ofNullable(currentDate),
                                    Optional.empty()));
                        }

                        // Reset for next batch
                        titleBuffer.clear();
                        amountBuffer.clear();
                    }

                    // Keep collecting titles
                    titleBuffer.add(line);
                }
            }

            // In case any amounts are left at the end
            if (!amountBuffer.isEmpty()) {
                int titleSize = titleBuffer.size();
                int amountSize = amountBuffer.size();

                for (int i = 0; i < amountSize; i++) {
                    String title = "";

                    // Look back from the end of titleBuffer for a title with letters
                    for (int j = titleBuffer.size() - 1 - i; j >= 0; j--) {
                        String candidate = titleBuffer.get(j).trim();
                        if (candidate.matches(".*[a-zA-Z].*")) {
                            title = candidate;
                            break;
                        }
                    }

                    transactions.add(new BoundaryTransactionFromFile(
                            Optional.of(amountBuffer.get(i)),
                            Optional.of(title),
                            Optional.ofNullable(currentDate),
                            Optional.empty()));
                }
            }

            return rebuildTransactionsWithCreatedAt(transactions, currentDate == null ? LocalDate.now() : LocalDate.parse(currentDate));
        });
    }

    private List<BoundaryTransactionFromFile> rebuildTransactionsWithCreatedAt(List<BoundaryTransactionFromFile> input, LocalDate createdAt) {
        return input.stream()
                .map(transaction -> new BoundaryTransactionFromFile(transaction.amount().map(amount -> amount * -1), transaction.title(), Optional.empty(), Optional.of(createdAt)))
                .collect(Collectors.toList());
    }

    private String tryParseAndFormatDate(String rawDate) {
        String[] patterns = {
                "dd.MM.yyyy", "dd-MM-yyyy", "dd/MM/yyyy",
                "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
        };

        for (String pattern : patterns) {
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
                LocalDate date = LocalDate.parse(rawDate, inputFormatter);
                return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException ignored) {}
        }

        return null; // Couldn't parse
    }

    private List<String> extractTextFromImage(byte[] fileBytes) {
        List<String> extractedTexts = new ArrayList<>();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.copyFrom(fileBytes);

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    List.of();
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page : annotation.getPagesList()) {
                    String pageText = "";
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word : para.getWordsList()) {
                                String wordText = "";
                                for (Symbol symbol : word.getSymbolsList()) {
                                    wordText = wordText + symbol.getText();
                                    System.out.format(
                                            "Symbol text: %s (confidence: %f)%n",
                                            symbol.getText(), symbol.getConfidence());
                                }
                                System.out.format(
                                        "Word text: %s (confidence: %f)%n%n", wordText, word.getConfidence());
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            System.out.println("%nParagraph: %n" + paraText);
                            System.out.format("Paragraph Confidence: %f%n", para.getConfidence());
                            blockText = blockText + paraText;
                        }
                        pageText = pageText + blockText;
                    }
                }
                String[] lines = annotation.getText().split("\\n");
                extractedTexts.addAll(Arrays.stream(lines).toList());
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return extractedTexts;
    }
}
