package com.tool.ppmtool.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ppmtool.domain.Backlog;
import com.tool.ppmtool.domain.Project;
import com.tool.ppmtool.domain.User;
import com.tool.ppmtool.exception.project.ProjectIdException;
import com.tool.ppmtool.model.response.ProjectResponse;
import com.tool.ppmtool.repository.ProjectRepository;
import com.tool.ppmtool.repository.UserRepository;
import com.tool.ppmtool.service.ProjectService;
import com.tool.ppmtool.shared.dto.ProjectDTO;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;


	@Override
	public ProjectDTO findProjectByIdentifier(String projectId, String username) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) 
				throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exists");
		
		if(project.getProjectLeader().equals(username)) {
			ProjectDTO returnValue = new ProjectDTO();
			BeanUtils.copyProperties(project, returnValue);
			return returnValue;
		}
		throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' restricted");
	}

	@Override
	public List<ProjectDTO> findAllProjects(String principal) {
		Iterable<Project> storedProjects = projectRepository.findAllByProjectLeader(principal);
		List<Project> list = 
				  StreamSupport.stream(storedProjects.spliterator(), false)
				    .collect(Collectors.toList());
		List<ProjectDTO> returnValue = new ArrayList<>();
		
		java.lang.reflect.Type listType = new TypeToken<List<ProjectDTO>>() {}.getType();
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(list, listType);
		
		return returnValue;
	}
	
//	 Shows ALL project in DB
//	@Override
//	public List<ProjectDTO> findAllProjects() {
//		Iterable<Project> storedProjects = projectRepository.findAll();
//		List<Project> list = 
//				  StreamSupport.stream(storedProjects.spliterator(), false)
//				    .collect(Collectors.toList());
//		List<ProjectDTO> returnValue = new ArrayList<>();
//		
//		java.lang.reflect.Type listType = new TypeToken<List<ProjectDTO>>() {}.getType();
//		ModelMapper modelMapper = new ModelMapper();
//		returnValue = modelMapper.map(list, listType);
//		
//		return returnValue;
//	}

	@Override
	public void deleteProjectByIdentifier(String projectId, String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (project == null)
			throw new ProjectIdException("No project with ID '" + projectId.toUpperCase() + "'");
		
		if(!project.getProjectLeader().equals(username)) 
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' restricted");
		
		projectRepository.deleteById(project.getId());
	}


	@Override
	public ProjectDTO saveOrUpdateProject(ProjectDTO projectDTO, String username) {
		Project project = new Project();
		Project storedProjectDetails = new Project();
		BeanUtils.copyProperties(projectDTO, project);
		
		
		
		project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
		storedProjectDetails = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
		if(storedProjectDetails != null)
			throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		
		{
			Backlog backlog = new Backlog();
			project.setBacklog(backlog);
			backlog.setProject(project);
			backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
		}
		

		//if user exists, add it to the project, user is logged
		User user = userRepository.findByUsername(username);
		project.setProjectLeader(user.getUsername());
		project.setUser(user);
		storedProjectDetails = projectRepository.save(project);

		
		ProjectDTO returnValue = new ProjectDTO();
		BeanUtils.copyProperties(storedProjectDetails, returnValue);
		
		return returnValue;
	}

	@Override
	public ProjectDTO updateProject(ProjectDTO projectDTO, String username) {
		Project newProject = new Project();
		
		BeanUtils.copyProperties(projectDTO, newProject);
		
		newProject.setProjectIdentifier(newProject.getProjectIdentifier().toUpperCase());
		Project projectEntity = projectRepository.findByProjectIdentifier(newProject.getProjectIdentifier());
		
		if (projectEntity == null)
			throw new ProjectIdException("No project with ID '" + newProject.getProjectIdentifier() + "'");
		
		if (!projectEntity.getProjectLeader().equals(username))
			throw new ProjectIdException("Project ID '" + projectEntity.getProjectIdentifier().toUpperCase() + "' restricted");
		
		projectEntity.setDescription(newProject.getDescription());
		projectEntity.setProjectName(newProject.getProjectName());
		projectEntity.setEnd_date(newProject.getEnd_date());
		projectEntity.setStart_date(newProject.getStart_date());
		Project updatedProject = projectRepository.save(projectEntity);
		ProjectDTO returnValue = new ProjectDTO();
		BeanUtils.copyProperties(updatedProject, returnValue);
		return returnValue;
	}

}
