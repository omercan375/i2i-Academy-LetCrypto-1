package org.example.i2iacademyletcrypto1.assets;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AssetsRepo extends CrudRepository<AssetsTable,UUID> {
    @Query("SELECT a FROM AssetsTable a WHERE a.symbol=:symbol")
    AssetsTable findBySymbol(@Param("symbol") String symbol);

    @Query("SELECT a FROM AssetsTable a")
    List<AssetsTable> findAll();


}
