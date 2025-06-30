package org.example.domain.transfer.repository;

import org.example.domain.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // 보낸 송금 내역 조회 (날짜 필터 없음) - 기본 한 달간
    @Query("SELECT t FROM Transfer t JOIN FETCH t.receiver WHERE t.sender.id = :userId ORDER BY t.transferredAt DESC")
    List<Transfer> findBySenderIdFetchJoin(@Param("userId") Long userId);

    // 보낸 송금 내역 조회 (날짜 필터 있음)
    @Query("SELECT t FROM Transfer t JOIN FETCH t.receiver WHERE t.sender.id = :userId AND t.transferredAt BETWEEN :from AND :to ORDER BY t.transferredAt DESC")
    List<Transfer> findBySenderIdAndTransferredAtBetweenFetchJoin(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // 모든 송금 내역 조회 (보낸 것 + 받은 것) - 통합 쿼리로 중복 제거
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

    // 친구 요청 수락용 - 받은 송금 내역만 조회
    @Query("SELECT t FROM Transfer t JOIN FETCH t.sender WHERE t.receiver.id = :userId AND t.transferredAt BETWEEN :from AND :to ORDER BY t.transferredAt DESC")
    List<Transfer> findByReceiverIdAndTransferredAtBetweenFetchJoin(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
