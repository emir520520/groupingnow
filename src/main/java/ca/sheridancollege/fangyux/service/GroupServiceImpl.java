package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    private GroupRepository groupRepo;

    @Override
    public List<SchoolGroup> getAllGroups() {
        return groupRepo.findAll();
    }

    @Override
    public SchoolGroup getGroupById(long id) {
        Optional<SchoolGroup> optional = groupRepo.findById(id);
        SchoolGroup group = null;
        if(optional.isPresent()) {
            group = optional.get();
        }else {
            throw new RuntimeException("Group with Id " + id+" is not found");
        }
        return group;
    }

    @Override
    public void deleteGroupById(long id) {
        this.groupRepo.deleteById(id);
    }

    @Override
    public Page<SchoolGroup> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.groupRepo.findAll(pageable);
    }

    @Override
    public void save(SchoolGroup group) {
        groupRepo.save(group);
    }
}