package com.vertigrated.sitd.auth;

import com.vertigrated.sitd.representation.Proctor;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public interface ProctorAuthenticator extends Authenticator<BasicCredentials,Proctor>
{
}
