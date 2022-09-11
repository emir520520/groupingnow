package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.SchoolGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupService {

    List<SchoolGroup> getAllGroups();

    SchoolGroup getGroupById(long id);

    void deleteGroupById(long id);

    Page<SchoolGroup> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    void save(SchoolGroup group);
}