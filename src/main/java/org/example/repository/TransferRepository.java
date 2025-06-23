package org.example.repository;

import org.example.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // 보낸 송금 내역 조회 (날짜 필터 없음)
    @Query("SELECT t FROM Transfer t JOIN FETCH t.receiver WHERE t.sender.id = :userId ORDER BY t.transferredAt DESC")
    List<Transfer> findBySenderIdFetchJoin(@Param("userId") Long userId);

    // 보낸 송금 내역 조회 (날짜 필터 있음)
    @Query("SELECT t FROM Transfer t JOIN FETCH t.receiver WHERE t.sender.id = :userId AND t.transferredAt BETWEEN :from AND :to ORDER BY t.transferredAt DESC")
    List<Transfer> findBySenderIdAndTransferredAtBetweenFetchJoin(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // 받은 송금 내역 조회 (거래 조회용)
    @Query("SELECT t FROM Transfer t JOIN FETCH t.sender WHERE t.receiver.id = :userId ORDER BY t.transferredAt DESC")
    List<Transfer> findByReceiverIdFetchJoin(@Param("userId") Long userId);

    // 받은 송금 내역 조회 (날짜 필터 있음)
    @Query("SELECT t FROM Transfer t JOIN FETCH t.sender WHERE t.receiver.id = :userId AND t.transferredAt BETWEEN :from AND :to ORDER BY t.transferredAt DESC")
    List<Transfer> findByReceiverIdAndTransferredAtBetweenFetchJoin(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // 보낸 송금과 받은 송금 모두 조회 (거래 조회용)
    @Query("""
        SELECT t FROM Transfer t 
        JOIN FETCH t.sender 
        JOIN FETCH t.receiver 
        WHERE (t.sender.id = :userId OR t.receiver.id = :userId) 
        ORDER BY t.transferredAt DESC
    """)
    List<Transfer> findAllByUserIdFetchJoin(@Param("userId") Long userId);

    // 보낸 송금과 받은 송금 모두 조회 (날짜 필터 있음)
    @Query("""
        SELECT t FROM Transfer t 
        JOIN FETCH t.sender 
        JOIN FETCH t.receiver 
        WHERE (t.sender.id = :userId OR t.receiver.id = :userId) 
        AND t.transferredAt BETWEEN :from AND :to 
        ORDER BY t.transferredAt DESC
    """)
    List<Transfer> findAllByUserIdAndTransferredAtBetweenFetchJoin(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
