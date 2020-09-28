package com.segurosbolivar.bienestar.model;

import java.util.List;

public class PureCloudResponse {

	private List<Error> errors;
    private Body body;

    public PureCloudResponse(){

    }

    public PureCloudResponse(Body body, List<Error> errors){
        this.body = body;
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
