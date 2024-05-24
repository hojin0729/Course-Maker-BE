package coursemaker.coursemaker.domain.tag.service;

import coursemaker.coursemaker.domain.tag.entity.CourseTag;
import coursemaker.coursemaker.domain.tag.entity.DestinationTag;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.repository.CourseTagRepository;
import coursemaker.coursemaker.domain.tag.repository.DestinationTagRepository;
import coursemaker.coursemaker.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
*TODO:
* 메소드 실패시 커스텀 예외처리 하기
* 여행지, 코스 도메인 서비스레이어 완성시 테스트 및 검증 진행
* */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final CourseTagRepository courseTagRepository;
    private final TagRepository tagRepository;
    private final DestinationTagRepository destinationTagRepository;

    private final CourseService courseService;
    private final DestinationService destinationService;

    /*****태그 기본 CRUD*****/

    @Override
    public Tag CreateTag(Tag tag){
        Tag savedTag = tagRepository.save(tag);
        return tagRepository.findById(savedTag.getId())
                .orElseThrow(() -> new RuntimeException("태그 생성 실패"));
    }

    @Override
    public Tag findById(Long id){
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("태그가 없습니다"));
    }

    @Override
    public Tag findByTagName(String name){
        return tagRepository.findByname(name)
                .orElseThrow(() -> new RuntimeException("태그가 없습니다"));
    }

    // ISSUE: 제대로 업데이트 됬는지 확인하고 싶은데 어떻게 해야 깔끔하게 할 수 있을까요? 아니면 굳이 검증을 해서 반환을 할 필요가 없을까요?
    @Override
    public Tag UpdateTag(Tag tag){
        Tag savedTag = tagRepository.save(tag);

        Tag updated = tagRepository.findById(savedTag.getId()).get();

        if(updated.equals(tag)){
            return updated;
        }else{
            throw new RuntimeException("태그 변경 실패");
        }
    }

    // ISSUE: 이것도 제대로 삭제됬는지 검증하는 절차가 필요할까요?
    @Override
    public void DeleteById(Long id){
        tagRepository.deleteById(id);
    }

    /******태그-코스 ******/
    /*TODO: 코스 도메인 엔티티, 서비스레이어 완성시 연결 및 검증*/

    // ISSUE: 코스에 맞는 태그들을 추가하는 메소드 입니다. 이때 어떤 반환값을 주는게 좋을까요?
    // 추가적으로, 태그가 제대로 추가됬는지 검증하는 로직이 필요할까요?
    @Override
    public void AddTagsByCourse(Long courseId, List<Long> tagIds){
        CourseTag courseTag = new CourseTag();

        if(tagIds.isEmpty()){
            throw new RuntimeException("태그가 없습니다.");
        }

        // 중복된 태그를 제외하고 추가함
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("태그가 없습니다."));

            if(courseTagRepository.findByCourseIdAndTagId(courseId, tagId).isEmpty()){
                courseTag.setTag(tag);
                courseTag.setCourseId(courseService.findById(courseId));
                courseTagRepository.save(courseTag);
            }
        }
    }

    @Override
    public List<Tag> findAllByCourseId(Long courseId){
        List<CourseTag> courseTags = courseTagRepository.findAllByCourseId(courseId);
        List<Tag> tags = new ArrayList<>();

        // CourseTag에서 태그 추출
        for(CourseTag courseTag : courseTags){
            tags.add(courseTag.getTag());
        }

        return tags;
    }

    // 특정 태그에 맞는 코스 검색
    @Override
    public List<Long> findAllCourseByTagId(Long tagId){
        List<TravelCourse> courses = new ArrayList<>();
        List<CourseTag> courseTags = courseTagRepository.findAllByTagId(tagId);

        for(CourseTag courseTag : courseTags){
            courses.add(courseTag.getCourse());
        }

        return courses;
    }

    // 동적쿼리 사용?
    @Override
    public void findCourseByTags(Long courseId, List<Tag> tags){
    }

    @Override
    public void deleteTagByCourse(Long courseId, List<Tag> tags){

        if(tags.isEmpty()){
            throw new RuntimeException("태그가 없습니다.");
        }

        // TODO: 코스 도메인 서비스레이어 삭제시 연결
        // 코스에 태그들 삭제
        for (Tag tag : tags) {
            if(courseTagRepository.findByCourseIdAndTagId(courseId, tag.getId()).isPresent()){
                courseTagRepository.deleteByCourseIdAndTagId(courseId, tag.getId());
            }
        }
    }

    @Override
    public void deleteAllTagByCourse(Long courseId){
        courseTagRepository.deleteAllByCourseId(courseId);
    }



    /******태그-여행지 ******/
    /*TODO: 여행지 도메인 엔티티, 서비스레이어 완성시 연결 및 검증*/
    @Override
    public void AddTagsByDestination(Long destinationId, List<Long> tagIds){
        DestinationTag destinationTag = new DestinationTag();

        if(tagIds.isEmpty()){
            throw new RuntimeException("태그가 없습니다.");
        }

        // 중복된 태그를 제외하고 추가함
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("태그가 없습니다."));

            if(destinationTagRepository.findByDestinationIdAndTagId(destinationId, tagId).isEmpty()){
                destinationTag.setTag(tag);
                destinationTag.setDestination(destinationService.findById(destinationId));
                destinationTagRepository.save(destinationTag);
            }
        }
    }

    @Override
    public List<Tag> findAllByDestinationId(Long destinationId){
        List<DestinationTag> destinationTags = destinationTagRepository.findAllByDestinationId(destinationId);
        List<Tag> tags = new ArrayList<>();

        // CourseTag에서 태그 추출
        for(DestinationTag destinationTag : destinationTags){
            tags.add(destinationTag.getTag());
        }

        return tags;
    }

    // 특정 태그에 맞는 코스 검색
    @Override
    public List<Destination> findAllDestinationByTagId(Long tagId){
        List<Destination> destinations = new ArrayList<>();
        List<DestinationTag> courseTags = destinationTagRepository.findAllByTagId(tagId);

        for(DestinationTag destinationTag : courseTags){
            destinations.add(destinationTag.getDestination());
        }

        return destinations;
    }

    // 동적쿼리 사용?
    @Override
    public void findDestinationByTags(Long destinationId, List<Tag> tags){
    }

    @Override
    public void deleteTagByDestination(Long destinationId, List<Tag> tags){

        if(tags.isEmpty()){
            throw new RuntimeException("태그가 없습니다.");
        }

        // TODO: 여행지 도메인 서비스레이어 삭제시 연결
        // 여행지에 포함된 태그들 삭제
        for (Tag tag : tags) {
            if(destinationTagRepository.findByDestinationIdAndTagId(destinationId, tag.getId()).isPresent()){
                destinationTagRepository.deleteByDestinationAndTagId(destinationId, tag.getId());
            }
        }
    }

    @Override
    public void deleteAllTagByDestination(Long destinationId){
        destinationTagRepository.deleteAllByDestinationId(destinationId);
    }

}