package com.vertigrated.sitd.auth;

import com.vertigrated.sitd.representation.Player;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public interface PlayerAuthenticator extends Authenticator<BasicCredentials, Player>
{
}
