package riwi.riwi.riwi_education.infraestructure.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequest;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.CoursesResponse;
import riwi.riwi.riwi_education.api.dto.response.UsersBasicResponse;
import riwi.riwi.riwi_education.domain.entities.Courses;
import riwi.riwi.riwi_education.domain.entities.Users;
import riwi.riwi.riwi_education.domain.repositories.CoursesRepository;
import riwi.riwi.riwi_education.domain.repositories.UsersRepository;
import riwi.riwi.riwi_education.infraestructure.abstract_service.ICoursesService;
import riwi.riwi.riwi_education.utils.enums.Role;
import riwi.riwi.riwi_education.utils.enums.SortType;
import riwi.riwi.riwi_education.utils.exceptions.BadRequestException;
import riwi.riwi.riwi_education.utils.exceptions.RoleDenegateException;

@Service
@AllArgsConstructor
public class CoursesService  implements ICoursesService {

    @Autowired
    private final CoursesRepository coursesRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Override
    public CoursesResponse create(CoursesRequest request) {

        Courses courses = this.requestToEntity(request);
        Users users = this.usersRepository.findById(request.getUserInstructor()).orElseThrow(()-> new BadRequestException("User"));

        courses.setUserInstructor(users);
        CoursesResponse coursesResponse = new CoursesResponse();
        if (!courses.getUserInstructor().getRole().equals(Role.INSTRUCTOR)){
            throw new RoleDenegateException("Instructor");
        }else {
            coursesResponse = this.entityToResp(this.coursesRepository.save(courses));
         }
        return coursesResponse;
    }

    @Override
    public CoursesResponse get(Integer integer) {
        return this.entityToResp(this.find(integer));
    }

    @Override
    public CoursesResponse update(CoursesRequest request, Integer integer) {
//        Courses courses = this.find(integer);
//
//        Courses coursesUpdate = this.requestToEntity(request);
//        coursesUpdate.setCourseId(integer);
//        Users users = this.usersRepository.findById(request.getUserInstructor()).orElseThrow(()-> new BadRequestException("User"));
//
//        coursesUpdate.setUserInstructor(users);
//        CoursesResponse coursesResponse = new CoursesResponse();
//        if (!coursesUpdate.getUserInstructor().getRole().equals(Role.INSTRUCTOR)){
//            throw new RoleDenegateException("Instructor");
//        }else {
//            coursesResponse = this.entityToResp(this.coursesRepository.save(coursesUpdate));
//        }
        return null;
    }

    @Override
    public void delete(Integer integer) {
        Courses courses = this.find(integer);
        this.coursesRepository.delete(courses);
    }

    @Override
    public Page<CoursesResponse> getAll(int page, int size, SortType sort) {

        if(page<0) page = 0;
        PageRequest pagination = null;

        switch (sort){
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.coursesRepository.findAll(pagination).map(this::entityToResp);
    }

    private CoursesResponse entityToResp(Courses entity){
        CoursesResponse coursesResponse = new CoursesResponse();
        BeanUtils.copyProperties(entity, coursesResponse);
        //Creamos un UsersBasicResponse le pasamos el user normal
        //Y se lo instanciamos a coursesResponse
        UsersBasicResponse usersBasic = new UsersBasicResponse();
        BeanUtils.copyProperties(entity.getUserInstructor(),usersBasic);
        coursesResponse.setUserInstructor(usersBasic);
        return coursesResponse;
    }

    private Courses requestToEntity(CoursesRequest request){
        Courses courses = new Courses();
        BeanUtils.copyProperties(request,courses);
        return courses;
    }
//    private Courses requestToEntityUpdate(CoursesRequestUpdate request){
//        Courses courses = new Courses();
//        BeanUtils.copyProperties(request,courses);
//        return courses;
//    }

    // private Courses requestToEntity(CoursesRequestUpdate request){
    //     Courses courses = new Courses();
    //     BeanUtils.copyProperties(request,courses);
    //     return courses;
    // }

    private Courses find(Integer id){
        return this.coursesRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("courses"));
    }

    @Override
    public CoursesResponse update(CoursesRequestUpdate request, Integer id) {
//        Courses courses = this.find(id);
//        System.out.println(courses);
//        Courses coursesUpdate = this.requestToEntityUpdate(request);
//
//        coursesUpdate.setCourseId(courses.getCourseId());
//        coursesUpdate.setUserInstructor(courses.getUserInstructor());
//        System.out.println("ffffffff  "+coursesUpdate);
//        return this.entityToResp(this.coursesRepository.save(coursesUpdate));
        return null;
    }
}
