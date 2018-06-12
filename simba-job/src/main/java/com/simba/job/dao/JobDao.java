package com.simba.job.dao;

import java.util.List;

import com.simba.framework.util.jdbc.Pager;
import com.simba.job.model.Job;

/**
 * 任务 Dao
 * 
 * @author caozj
 * 
 */
public interface JobDao {

  void add(Job job);

  void update(Job job);

  void updateWithoutExeCount(Job job);

  void delete(int id);

  void updateStatus(int id, String status);

  void incrExeCount(int id);

  List<Job> listAll();

  int count();

  int countBy(String field, Object value);

  List<Job> page(Pager page);

  Job get(int id);

  Job getBy(String field, Object value);

  Job getByAnd(String field1, Object value1, String field2, Object value2);

  Job getByOr(String field1, Object value1, String field2, Object value2);

  List<Job> listBy(String field, Object value);

  List<Job> listByAnd(String field1, Object value1, String field2, Object value2);

  List<Job> listByOr(String field1, Object value1, String field2, Object value2);

  List<Job> pageBy(String field, Object value, Pager page);

  List<Job> pageByAnd(String field1, Object value1, String field2, Object value2, Pager page);

  List<Job> pageByOr(String field1, Object value1, String field2, Object value2, Pager page);

}
