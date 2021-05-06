package com.server.pollingapp.repository;

import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.models.PollStatus;
import com.server.pollingapp.models.PollsCategory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PollRepository extends JpaRepository<PollModel,String> {


   @Cacheable("Polls")
  //  @Query(value = "SELECT polls FROM PollModel polls WHERE polls.pollStatus = ?1",nativeQuery = true)
    List<PollModel> findAllByPollStatusEquals(PollStatus pollStatus);

    @Cacheable("ScheduledPolls")
    //@Query(value = "SELECT polls FROM PollModel polls WHERE polls.category=?1",nativeQuery = true)
    List<PollModel> findAllByCategoryEquals(PollsCategory pollsCategory);

    PollModel findByQuestion(String question);

}
