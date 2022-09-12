package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.SchoolGroup;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupService {

    List<SchoolGroup> getAllGroups();

    SchoolGroup getGroupById(long id);

    void deleteGroupById(long id);

    Page<SchoolGroup> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    Page<SchoolGroup> getGroupsPaginated(int pageNo, int pageSize, String scope);

    void save(SchoolGroup group);
}