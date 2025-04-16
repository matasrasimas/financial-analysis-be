package org.example.route;

import io.javalin.http.UploadedFile;
import io.reactivex.rxjava3.core.Single;
import org.example.converter.TransactionFromFileB2RConverter;
import org.example.exception.JavalinExceptionHandler;
import org.example.factory.AuthenticationUseCaseFactory;
import org.example.factory.TransactionUseCaseFactory;
import org.example.model.boundary.BoundaryTransactionFromFile;
import org.example.model.rest.RestTransactionFromFile;
import org.example.serialization.json.JsonSerializer;

import java.io.IOException;
import java.util.List;

public class GenerateTransactionsFromFileRoute extends AuthedRoute<List<BoundaryTransactionFromFile>, List<RestTransactionFromFile>> {
    private final TransactionUseCaseFactory transactionUseCaseFactory;
    private final TransactionFromFileB2RConverter transactionFromFileB2RConverter;

    public GenerateTransactionsFromFileRoute(AuthenticationUseCaseFactory authUCFactory,
                                             JsonSerializer jsonSerializer,
                                             JavalinExceptionHandler exceptionHandler,
                                             TransactionUseCaseFactory transactionUseCaseFactory,
                                             TransactionFromFileB2RConverter transactionFromFileB2RConverter) {
        super(authUCFactory, jsonSerializer, null, exceptionHandler);
        this.transactionUseCaseFactory = transactionUseCaseFactory;
        this.transactionFromFileB2RConverter = transactionFromFileB2RConverter;
    }

    @Override
    protected List<RestTransactionFromFile> convert(List<BoundaryTransactionFromFile> input) {
        return transactionFromFileB2RConverter.process(input);
    }

    @Override
    protected Single<List<BoundaryTransactionFromFile>> processAuthedRequest(RequestWrapper request) {
        try {
            UploadedFile uploadedFile = request.parseFile();
            byte[] fileBytes = uploadedFile.content().readAllBytes();
            return transactionUseCaseFactory.createGenerateTransactionsFromFileUseCase().execute(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
