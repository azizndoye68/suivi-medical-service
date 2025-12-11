package sn.diabete.suivimedical.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDto {
    private Long userId;
    private String title;
    private String message;
    private String channel; // "EMAIL", "SMS", "PUSH", "IN_APP"
    private String priority; // "HIGH", "MEDIUM", "LOW"
}
