package com.tool.ppmtool.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ppmtool.domain.Backlog;
import com.tool.ppmtool.domain.Project;
import com.tool.ppmtool.domain.ProjectTask;
import com.tool.ppmtool.exception.backlog.BacklogIdentifierException;
import com.tool.ppmtool.exception.project.ProjectIdException;
import com.tool.ppmtool.model.response.ProjectResponse;
import com.tool.ppmtool.repository.BacklogRepository;
import com.tool.ppmtool.repository.ProjectRepository;
import com.tool.ppmtool.repository.ProjectTaskRepository;
import com.tool.ppmtool.service.ProjectTaskService;
import com.tool.ppmtool.shared.dto.ProjectDTO;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	private ProjectServiceImpl projectService;

	@Override
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		// PT to be added to a specific project => backlog exists

		//use existing method to look for the project and extract the backlog from the received project
		ProjectDTO projectDTO = projectService.findProjectByIdentifier(projectIdentifier, username);
		
		Project project = new Project();
		//copy properties from projectDTO to project type
		BeanUtils.copyProperties(projectDTO, project);
		
		Backlog backlog = project.getBacklog();


		// set the backlog to the project task
		projectTask.setBacklog(backlog);

		// sequence will be PRJID-1
		Integer backlogSequence = backlog.getPTSequence();

		// update Backlog sequence
		backlogSequence++;

		backlog.setPTSequence(backlogSequence);

		// add sequence to projectTask
		projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);

		// set initial priority
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}

		// set initial status
		if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}

		ProjectTask savedValue = projectTaskRepository.save(projectTask);

		return savedValue;
	}

	@Override
	public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {

		//search for the project
		projectService.findProjectByIdentifier(backlog_id, username);
		
		//if the project exists, it has backlog
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);

	}

	@Override
	public ProjectTask findPTByProjectSequence(String backlog_id, String sequence, String username) {

		// check we are searching in an existing backlog
		projectService.findProjectByIdentifier(backlog_id, username);

		// check that the task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
		if (projectTask == null)
			throw new BacklogIdentifierException("Project Task with ID: '" + sequence + "' not found");

		// check that the received id's corresponds to the right project
		if (!projectTask.getBacklog().getProjectIdentifier().equals(backlog_id.toUpperCase()))
			throw new BacklogIdentifierException(
					"Project Task with ID: '" + sequence + "' does not exist in project: " + backlog_id);

		return projectTask;
	}
	

	@Override
	public ProjectTask updateProjectTask(String backlog_id, String projectSequence, ProjectTask projectTask, String username) {

		// check that the task exists
		ProjectTask savedProjectTask = findPTByProjectSequence(backlog_id,projectSequence, username);
		
		// if valid priority is received, change it
		if (projectTask.getPriority() != null && projectTask.getPriority() != 0) {
			savedProjectTask.setPriority(projectTask.getPriority());
		}
		
		// if valid due date is received, change it
		if (projectTask.getDueDate() != null ) {
			savedProjectTask.setDueDate(projectTask.getDueDate());
		}
		savedProjectTask.setStatus(projectTask.getStatus());
		savedProjectTask.setSummary(projectTask.getSummary());
		savedProjectTask.setAcceptanceCriteria(projectTask.getAcceptanceCriteria());
		savedProjectTask.setName(projectTask.getName());
		
		projectTaskRepository.save(savedProjectTask);
		return savedProjectTask;
	}

	@Override
	public void deleteProjectTask(String backlog_id, String sequence, String username) {

//		 check that the task exists
		ProjectTask storedProjectTask = findPTByProjectSequence(backlog_id,sequence, username);
		projectTaskRepository.delete(storedProjectTask);
	}

}
