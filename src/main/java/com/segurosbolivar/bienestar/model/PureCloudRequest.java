package com.segurosbolivar.bienestar.model;

import java.util.List;

public class PureCloudRequest {

	
	private List<String> callbackNumbers;
    private String documentType;
    private String documentNumber;

    public List<String> getCallbackNumbers() {
        return callbackNumbers;
    }

    public void setCallbackNumbers(List<String> callbackNumbers) {
        this.callbackNumbers = callbackNumbers;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
