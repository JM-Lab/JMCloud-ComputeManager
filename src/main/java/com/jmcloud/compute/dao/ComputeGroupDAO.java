package com.jmcloud.compute.dao;

public interface ComputeGroupDAO<CGVO, DS> {

	public boolean createComputeGroupData(CGVO computeGroupVO);

	public CGVO readComputeGroupData(DS dataSource);

	public boolean updateComputeGroupData(CGVO computeGroupVO);

	public boolean deleteComputeGroupData(CGVO computeGroupVO);

}
