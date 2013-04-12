package com.jmcloud.compute.manager;

import java.util.Map;

public interface ComputeManager<CVO, CI> {
	public CVO createCompute(Map<String, String> requiredValues);

	public CVO[] createComputes(Map<String, String> requiredValues,
			int numOfComputes);

	public CVO provisionCompute(CVO computeVO);

	public CVO[] provisionComputes(CVO... computeVOs);

	public CI getComputeInfo(CVO computeVO);

	public CI[] getComputesInfo(CVO... computeVOs);

	public boolean terminateComputes(CVO... computeVOs);

	public boolean startComputes(CVO... computeVOs);

	public boolean rebootComputes(CVO... computeVOs);

	public boolean stopComputes(CVO... computeVOs);

	public CI monitoringCompute(CVO computeVO);

	public CI[] monitoringComputes(CVO... computeVOs);

	public CVO updateComputeStatus(CVO computeVO, String computeInfo);

	public CVO changeComputeName(CVO computeVO, String newComputeName);
}
