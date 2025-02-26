package com.withSchool.dto.school;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@Data
@ToString
public class ReqSchoolInformationSaveDTO {
    private SchoolInformationNoPaymentStateDTO schoolInformationNoPaymentStateDTO;
    private String adminEmail;
}
