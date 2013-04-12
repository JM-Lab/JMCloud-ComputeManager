package com.jmcloud.compute.aws.ec2.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.util.EC2Util;
import com.jmcloud.compute.dao.ComputeGroupDAO;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.sys.SystemString;
import com.jmcloud.compute.vo.ComputeGroupVO;

@Service("eC2GroupDAOWithJSON")
public class EC2GroupDAOWithJSON implements
		ComputeGroupDAO<ComputeGroupVO, Path> {

	@Override
	public boolean createComputeGroupData(ComputeGroupVO computeGroupVO) {
		Path jsonFilePath = getJsonFilePath(computeGroupVO);
		if (jsonFilePath.toFile().exists()) {
			return false;
		}
		try {
			Files.createDirectories(Paths.get(SystemEnviroment.getDataSaveDir()));
			Files.createFile(jsonFilePath);
			Files.write(jsonFilePath,
					EC2Util.gson.toJson(computeGroupVO, ComputeGroupVO.class)
							.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public ComputeGroupVO readComputeGroupData(Path jsonFilePath) {
		String json;
		try {
			json = new String(Files.readAllBytes(jsonFilePath), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return EC2Util.gson.fromJson(json, ComputeGroupVO.class);
	}

	@Override
	public boolean updateComputeGroupData(ComputeGroupVO computeGroupVO) {
		return deleteComputeGroupData(computeGroupVO) ? createComputeGroupData(computeGroupVO)
				: false;
	}

	@Override
	public boolean deleteComputeGroupData(ComputeGroupVO computeGroupVO) {
		Path filePath = getJsonFilePath(computeGroupVO);
		if (!filePath.toFile().exists()) {
			return true;
		}
		try {
			return Files.deleteIfExists(getJsonFilePath(computeGroupVO));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Path getJsonFilePath(ComputeGroupVO computeGroupVO) {
		return Paths.get(SystemEnviroment.getDataSaveDir(),
				computeGroupVO.getRegion() + computeGroupVO.getSecurityGroup()
						+ SystemString.JMCLOUD_SIGNATURE);
	}
}
