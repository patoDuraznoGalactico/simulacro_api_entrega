package riwi.riwi.riwi_education.infraestructure.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import riwi.riwi.riwi_education.api.dto.request.EnrollmentRequest;
import riwi.riwi.riwi_education.api.dto.response.CoursesBasicResponse;
import riwi.riwi.riwi_education.api.dto.response.EnrollmentResponse;
import riwi.riwi.riwi_education.api.dto.response.UsersBasicResponse;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Enrollments;
import riwi.riwi.riwi_education.domain.entities.Users;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.EnrollmentsRepository;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.IEnrollmentService;
import riwi.riwi.riwi_education.utils.enums.Role;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;
import riwi.riwi.riwi_education.utils.exceptions.RoleDenegateException;

@Service
@AllArgsConstructor
public class EnrollmentsService implements IEnrollmentService   {
    @Autowired
    private final EnrollmentsRepository enrollmentsRepository;
    @Autowired
    private final CoursesRepository coursesRepository;
    @Autowired
    private final UsersRepository usersRepository;


    @Override
    public EnrollmentResponse create(EnrollmentRequest request) {
        Enrollments enrollments = this.requestToEntity(request);
        Users users = this.usersRepository.findById(request.getUserId()).orElseThrow(()-> new BadRequestException("User"));
        Courses courses = this.coursesRepository.findById(request.getCourseId()).orElseThrow(()-> new BadRequestException("Course"));

        enrollments.setUser(users);
        enrollments.setCourse(courses);
        EnrollmentResponse enrollmentResponse = new EnrollmentResponse();
        if (!enrollments.getUser().getRole().equals(Role.STUDENT)){
            throw new RoleDenegateException("Estudiante");
        }else {
            enrollmentResponse = this.entityToResp(this.enrollmentsRepository.save(enrollments));
            System.out.println(enrollments);
        }
        System.out.println(enrollmentResponse);
        return enrollmentResponse;
    }

    @Override
    public EnrollmentResponse get(Integer integer) {
        return this.entityToResp(this.find(integer));
    }

    @Override
    public EnrollmentResponse update(EnrollmentRequest request, Integer integer) {
        Users users = this.usersRepository.findById(request.getUserId()).orElseThrow(()-> new BadRequestException("User"));
        Courses courses = this.coursesRepository.findById(request.getCourseId()).orElseThrow(()-> new BadRequestException("Course"));
        Enrollments enrollments = this.find(integer);

        Enrollments enrollmentsUpdate =  this.requestToEntity(request);

        enrollmentsUpdate.setEnrollmentId(integer);
        enrollmentsUpdate.setUser(users);
        enrollmentsUpdate.setCourse(courses);
        EnrollmentResponse enrollmentResponse = new EnrollmentResponse();
        if (!enrollmentsUpdate.getUser().getRole().equals(Role.STUDENT)){
            throw new RoleDenegateException("Estudinte");
        }else {
            enrollmentResponse = this.entityToResp(this.enrollmentsRepository.save(enrollmentsUpdate));
        }

        return enrollmentResponse;
    }

    @Override
    public void delete(Integer integer) {
        Enrollments enrollments = this.find(integer);
        this.enrollmentsRepository.delete(enrollments);
    }

    @Override
    public Page<EnrollmentResponse> getAll(int page, int size, SortType sort) {
        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.enrollmentsRepository.findAll(pagination).map(this::entityToResp);
    }

    private EnrollmentResponse entityToResp(Enrollments entity){
        EnrollmentResponse enrollmentResponse = new EnrollmentResponse();
        BeanUtils.copyProperties(entity,enrollmentResponse);

        UsersBasicResponse usersBasic = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUser(),usersBasic);
        CoursesBasicResponse coursesBasic = new CoursesBasicResponse();
        BeanUtils.copyProperties(entity.getCourse(),coursesBasic);

        UsersBasicResponse instructorBasic = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getCourse().getUserInstructor(),instructorBasic);
        coursesBasic.setUserInstructor(instructorBasic);


        enrollmentResponse.setUser(usersBasic);
        enrollmentResponse.setCourse(coursesBasic);
        return enrollmentResponse;
    }

    private Enrollments requestToEntity(EnrollmentRequest request){
        Enrollments enrollments = new Enrollments();
        BeanUtils.copyProperties(request,enrollments);
        return enrollments;
    }

    private Enrollments find(Integer id){
        return this.enrollmentsRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("enrollments"));
    }
}
