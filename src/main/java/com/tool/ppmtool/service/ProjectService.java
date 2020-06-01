package com.tool.ppmtool.service;

import java.util.List;

import com.tool.ppmtool.domain.Project;
import com.tool.ppmtool.shared.dto.ProjectDTO;

public interface ProjectService {

	ProjectDTO saveOrUpdateProject(ProjectDTO projectDTO, String username);
	
	ProjectDTO findProjectByIdentifier(String projectId, String username);
	
	List<ProjectDTO> findAllProjects(String principal);
	
	void deleteProjectByIdentifier(String projectId, String username);
	
	ProjectDTO updateProject(ProjectDTO project, String username);
}
