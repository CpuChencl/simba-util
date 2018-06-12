package com.simba.job.dao.impl;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simba.framework.util.jdbc.Jdbc;
import com.simba.framework.util.jdbc.Pager;
import com.simba.framework.util.jdbc.StatementParameter;
import com.simba.job.dao.JobDao;
import com.simba.job.model.Job;

/**
 * 任务 Dao实现类
 * 
 * @author caozj
 * 
 */
@Repository
public class JobDaoImpl implements JobDao {

  @Autowired
  private Jdbc jdbc;

  private static final String table = "job";

  @Override
  public void add(Job job) {
    String sql = "insert into " + table
        + "( name, description, cronExpression, startTime, endTime, exeCount, maxExeCount, status, className, methodName, delayTime, intervalTime) values(?,?,?,?,?,?,?,?,?,?,?,?)";
    Number id = jdbc.updateForGeneratedKey(sql, job.getName(), job.getDescription(),
        job.getCronExpression(), job.getStartTime(), job.getEndTime(), job.getExeCount(),
        job.getMaxExeCount(), job.getStatus(), job.getClassName(), job.getMethodName(),
        job.getDelayTime(), job.getIntervalTime());
    job.setId(NumberUtils.toInt(id + ""));
  }

  @Override
  public void update(Job job) {
    String sql = "update " + table
        + " set  name = ? , description = ? , cronExpression = ? , startTime = ? , endTime = ? , exeCount = ? , maxExeCount = ? , status = ? , className = ? , methodName = ? , delayTime = ? , intervalTime = ?  where id = ?  ";
    jdbc.updateForBoolean(sql, job.getName(), job.getDescription(), job.getCronExpression(),
        job.getStartTime(), job.getEndTime(), job.getExeCount(), job.getMaxExeCount(),
        job.getStatus(), job.getClassName(), job.getMethodName(), job.getDelayTime(),
        job.getIntervalTime(), job.getId());
  }

  @Override
  public void updateWithoutExeCount(Job job) {
    String sql = "update " + table
        + " set  name = ? , description = ? , cronExpression = ? , startTime = ? , endTime = ? ,maxExeCount = ? , status = ? , className = ? , methodName = ? , delayTime = ? , intervalTime = ?  where id = ?  ";
    jdbc.updateForBoolean(sql, job.getName(), job.getDescription(), job.getCronExpression(),
        job.getStartTime(), job.getEndTime(), job.getMaxExeCount(), job.getStatus(),
        job.getClassName(), job.getMethodName(), job.getDelayTime(), job.getIntervalTime(),
        job.getId());
  }

  @Override
  public void delete(int id) {
    String sql = "delete from " + table + " where id = ? ";
    jdbc.updateForBoolean(sql, id);
  }

  @Override
  public List<Job> page(Pager page) {
    String sql = "select * from " + table;
    return jdbc.queryForPage(sql, Job.class, page);
  }

  @Override
  public List<Job> listAll() {
    String sql = "select * from " + table;
    return jdbc.queryForList(sql, Job.class);
  }

  @Override
  public int count() {
    String sql = "select count(*) from " + table;
    return jdbc.queryForInt(sql);
  }

  @Override
  public Job get(int id) {
    String sql = "select * from " + table + " where id = ? ";
    return jdbc.query(sql, Job.class, id);
  }

  @Override
  public Job getBy(String field, Object value) {
    String sql = "select * from " + table + " where " + field + " = ? ";
    return jdbc.query(sql, Job.class, value);
  }

  @Override
  public Job getByAnd(String field1, Object value1, String field2, Object value2) {
    String sql = "select * from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
    return jdbc.query(sql, Job.class, value1, value2);
  }

  @Override
  public Job getByOr(String field1, Object value1, String field2, Object value2) {
    String sql = "select * from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
    return jdbc.query(sql, Job.class, value1, value2);
  }

  @Override
  public List<Job> listBy(String field, Object value) {
    String sql = "select * from " + table + " where " + field + " = ? ";
    return jdbc.queryForList(sql, Job.class, value);
  }

  @Override
  public List<Job> listByAnd(String field1, Object value1, String field2, Object value2) {
    String sql = "select * from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
    return jdbc.queryForList(sql, Job.class, value1, value2);
  }

  @Override
  public List<Job> listByOr(String field1, Object value1, String field2, Object value2) {
    String sql = "select * from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
    return jdbc.queryForList(sql, Job.class, value1, value2);
  }

  @Override
  public List<Job> pageBy(String field, Object value, Pager page) {
    String sql = "select * from " + table + " where " + field + " = ? ";
    StatementParameter param = new StatementParameter();
    param.set(value);
    return jdbc.queryForPage(sql, Job.class, page, param);
  }

  @Override
  public List<Job> pageByAnd(String field1, Object value1, String field2, Object value2,
      Pager page) {
    String sql = "select * from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
    StatementParameter param = new StatementParameter();
    param.set(value1);
    param.set(value2);
    return jdbc.queryForPage(sql, Job.class, page, param);
  }

  @Override
  public List<Job> pageByOr(String field1, Object value1, String field2, Object value2,
      Pager page) {
    String sql = "select * from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
    StatementParameter param = new StatementParameter();
    param.set(value1);
    param.set(value2);
    return jdbc.queryForPage(sql, Job.class, page, param);
  }

  @Override
  public int countBy(String field, Object value) {
    String sql = "select count(*) from " + table + " where " + field + " = ? ";
    return jdbc.queryForInt(sql, value);
  }

  @Override
  public void updateStatus(int id, String status) {
    String sql = "update " + table + " set status = ? where id = ? ";
    jdbc.updateForBoolean(sql, status, id);
  }

  @Override
  public void incrExeCount(int id) {
    String sql = "update " + table + " set exeCount = exeCount + 1 where id = ? ";
    jdbc.updateForBoolean(sql, id);
  }

}
