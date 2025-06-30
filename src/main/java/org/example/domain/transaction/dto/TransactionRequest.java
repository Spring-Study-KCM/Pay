package org.example.domain.transaction.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

    @Min(value = 0, message = "페이지는 0 이상이어야 합니다")
    private int page = 0;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
    private int size = 20;

    // 기본값 설정: from이 null이면 한 달 전으로 설정
    public LocalDate getFrom() {
        return from != null ? from : LocalDate.now().minusMonths(1);
    }

    // 기본값 설정: to가 null이면 오늘로 설정
    public LocalDate getTo() {
        return to != null ? to : LocalDate.now();
    }
}
