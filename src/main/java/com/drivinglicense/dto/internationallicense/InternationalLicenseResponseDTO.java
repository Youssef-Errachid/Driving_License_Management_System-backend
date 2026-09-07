package  com.drivinglicense.dto.internationallicense;

import com.drivinglicense.enums.InternationalPermitStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternationalLicenseResponseDTO {
    private Long id;
    private LocalDate issueDate;
    private InternationalPermitStatus internationalPermitStatus;
    private Long licenseId;
    private  String licenseNumber;
    private Long driverId;
}