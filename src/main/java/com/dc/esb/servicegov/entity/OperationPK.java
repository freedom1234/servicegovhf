package com.dc.esb.servicegov.entity;

import java.io.Serializable;

public class OperationPK  implements Serializable{

	private static final long serialVersionUID = 7599992430051310574L;

	private String serviceId;
	private String operationId;
	
	public OperationPK(){}
	
	public OperationPK(String serviceId, String operationId) {
		super();
		this.serviceId = serviceId;
		this.operationId = operationId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (serviceId == null ? 0 : serviceId.hashCode());
		result = PRIME * result + (operationId ==null ? 0 : operationId.hashCode());
		return result;
	}

	/**
	 * 覆盖equals方法，必须要有
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof OperationPK)) return false;
		OperationPK objKey = (OperationPK)obj;
		if(serviceId.equalsIgnoreCase(objKey.serviceId) &&
				operationId.equalsIgnoreCase(objKey.operationId)) {
			return true;
		}
		return false;
	}
	
}
