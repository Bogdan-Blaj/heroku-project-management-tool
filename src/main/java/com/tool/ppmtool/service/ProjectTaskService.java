package com.tool.ppmtool.service;

import com.tool.ppmtool.domain.ProjectTask;

public interface ProjectTaskService {

	ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username);

	Iterable<ProjectTask> findBacklogById(String backlog_id, String username);
	
	ProjectTask findPTByProjectSequence(String backlog_id, String sequence, String username);
	
	ProjectTask updateProjectTask(String backlog_id, String projectSequence, ProjectTask projectTask, String username);
	
	void deleteProjectTask(String backlog_id, String sequence, String username);
}
