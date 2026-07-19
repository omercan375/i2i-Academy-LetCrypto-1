package org.example.i2iacademyletcrypto1.price_history;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PriceHistoryRepo extends CrudRepository<PriceHistoryTable, UUID> {

    @Query(value = """
SELECT *
FROM price_history
WHERE asset_id=:assetId
ORDER BY recorded_at DESC
LIMIT 1
""", nativeQuery = true)
    PriceHistoryTable findOneByAssetId(UUID assetId);

    @Query("SELECT p FROM PriceHistoryTable p WHERE p.asset.id=:assetId")
    List<PriceHistoryTable> findAllByAssetId(UUID assetId);


}
