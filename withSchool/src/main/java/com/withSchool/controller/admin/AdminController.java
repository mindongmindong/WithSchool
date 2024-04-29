package com.withSchool.controller.admin;

import com.withSchool.dto.classes.ClassDTO;
import com.withSchool.entity.classes.ClassInformation;
import com.withSchool.service.classes.ClassService;
import com.withSchool.entity.subject.Subject;
import com.withSchool.entity.user.User;
import com.withSchool.service.subject.SubjectService;
import com.withSchool.service.user.UserService;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@EnableAutoConfiguration
@RequestMapping("/admin")
public class AdminController {
    private final SubjectService subjectService;
    private final UserService userService;
    private final ClassService classService;

    @PostMapping("/subjects")
    public ResponseEntity<String> createSubject(@RequestParam String subjectName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(authentication.getName());
        if (user == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).body("해당하는 유저가 없습니다.");

        try {
            Subject subject = subjectService.saveSubject(subjectName, user);
            return ResponseEntity.ok().body(subject.getSubjectName() + " 수업이 생성되었습니다.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @DeleteMapping("/subjects/{subjectId}")
    public ResponseEntity<String> deleteOneSubject(@PathVariable Long subjectId) {
        try {
            subjectService.deleteById(subjectId);
            return ResponseEntity.ok().body("해당 과목이 삭제되었습니다.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/classes/byUser")
    public ResponseEntity<List<ClassInformation>> getAllClasses() {
        List<ClassInformation> classes = classService.findBySchoolInformation_SchoolId();
        return ResponseEntity.ok().body(classes);
    }

    @GetMapping("/classes/byUser/{grade}")
    public ResponseEntity<List<ClassInformation>> getAllClassesByGrade(@PathVariable int grade) {
        List<ClassInformation> classes = classService.findBySchoolInformation_SchoolIdAndGrade(grade);
        return ResponseEntity.ok().body(classes);
    }

    @GetMapping("/classes/byUser/{grade}/{inClass}")
    public ResponseEntity<Optional<ClassInformation>> getClassByGradeAndInClass(@PathVariable int grade, @PathVariable int inClass) {
        Optional<ClassInformation> searchedClass = classService.findBySchoolInformation_SchoolIdAndGradeAndInClass(grade, inClass);
        return ResponseEntity.ok().body(searchedClass);
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<Optional<ClassInformation>> getClassById(@PathVariable Long classId) {
        Optional<ClassInformation> classInfo = classService.getClassById(classId);
        return ResponseEntity.ok().body(classInfo);
    }

    @PatchMapping("/classes/{classId}/update")
    public ResponseEntity<String> updateClassInformation(@PathVariable Long classId, @RequestBody ClassDTO updatedClassDTO) {
        classService.updateClassInformation(classId, updatedClassDTO);
        return ResponseEntity.ok().body("해당 반이 수정되었습니다.");
    }

    @DeleteMapping("/classes/{classId}/delete")
    public ResponseEntity<String> deleteClassInformation(@PathVariable Long classId) {
        classService.deleteClassInformation(classId);
        return ResponseEntity.ok().body("해당 반이 삭제되었습니다.");
    }

}
