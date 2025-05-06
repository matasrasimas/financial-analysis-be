package org.example.usecase;

import io.reactivex.rxjava3.core.Single;
import org.example.model.boundary.BoundaryStatistics;

import java.util.UUID;

public interface RetrieveStatisticsUseCase {
    Single<BoundaryStatistics> execute(UUID orgId);
}
