package  com.drivinglicense.dto.internationallicense;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternationalLicenseCreateDTO {
    @NotNull(message = "license id is required")
    private Long licenseId;

    @NotNull(message = "driver id is required")
    private Long driverId;
}