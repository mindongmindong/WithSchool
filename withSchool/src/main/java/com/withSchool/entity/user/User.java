package com.withSchool.entity.user;

import com.withSchool.dto.user.BasicUserInfoDTO;
import com.withSchool.dto.user.ResUserDefaultDTO;
import com.withSchool.dto.user.ResUserUsercodeDTO;
import com.withSchool.entity.base.BaseEntity;
import com.withSchool.entity.classes.ClassInformation;
import com.withSchool.entity.school.SchoolInformation;
import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
@SuperBuilder
@AllArgsConstructor
@ToString
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    @Comment("사용자 PK")
    private Long userId;

    @Column(name = "id", unique = true)
    @Comment("로그인 ID")
    private String id;

    @Setter
    @Column(name = "password")
    @Comment("비밀번호")
    private String password;

    @Column(name = "email")
    @Comment("이메일")
    private String email;

    @Column(name = "name")
    @Comment("이름")
    private String name;

    @Column(name = "sex", columnDefinition = "TINYINT(1) default 0")
    @Comment("""
            성별
                        
            0 - 남자
            1 - 여자
            """)
    private Boolean sex;

    @Column(name = "phone_number", unique = true)
    @Comment("전화번호")
    private String phoneNumber;

    @Column(name = "address")
    @Comment("주소")
    private String address;

    @Column(name = "birth_date", length = 6)

    @Comment("생일")
    private String birthDate;

    @Column(name = "account_type")
    @Comment("""
            계정 구분
            
            0 - 학생
            1 - 학부모
            2 -  교사
            3 - 어드민
            4 - 슈퍼 어드민
            """)
    private int accountType;

    @Column(name = "user_code", unique = true)
    @Comment("사용자 코드")
    private String userCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @Comment("사용자가 속한 학교 PK")
    private SchoolInformation schoolInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classs_id")
    @Comment("사용자가 속한 반")
    private ClassInformation classInformation;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String auth = "ROLE_";
        if (this.accountType == 0) auth += "STUDENT";
        else if (this.accountType == 1) auth += "PARENT";
        else if (this.accountType == 2) auth += "TEACHER";
        else if (this.accountType == 3) auth += "ADMIN";
        else if (this.accountType == 4) auth += "SUPER";

        return Collections.singletonList(new SimpleGrantedAuthority(auth));
    }

    @Override
    public String getUsername() {
        return this.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void changeUserInfo(String email, String phoneNumber, String address) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    public void changeUserPassword(String password) {
        this.password = password;
    }

    public ResUserDefaultDTO toResUserDefaultDTO(){
        return ResUserDefaultDTO.builder()
                .userId(this.getUserId())
                .name(this.getName())
                .userName(this.getUsername())
                .build();
    }

    public ResUserUsercodeDTO toResUserUsercodeDTO(){
        return ResUserUsercodeDTO.builder()
                .userId(this.getUserId())
                .name(this.getName())
                .userName(this.getId())
                .userCode(this.getUserCode())
                .build();
    }
    public BasicUserInfoDTO entityToBasicUserInfoDTO(){
        Long classId = Optional.ofNullable(this.getClassInformation())
                .map(ClassInformation::getClassId)
                .orElse(null);

        Long schoolId = Optional.ofNullable(this.getSchoolInformation())
                .map(SchoolInformation::getSchoolId)
                .orElse(null);

        return BasicUserInfoDTO.builder()
                .userId(this.getUserId())
                .id(this.getUsername())
                .name(this.getName())
                .email(this.getEmail())
                .address(this.getAddress())
                .phoneNumber(this.getPhoneNumber())
                .sex(this.getSex())
                .classId(classId)
                .schoolId(schoolId)
                .build();
    }
    public void updateClassInfo(ClassInformation classInformation){
        this.classInformation = classInformation;
    }

}
