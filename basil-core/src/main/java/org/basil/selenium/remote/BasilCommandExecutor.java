/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.remote;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openqa.selenium.remote.DriverCommand.GET_ALL_SESSIONS;
import static org.openqa.selenium.remote.DriverCommand.NEW_SESSION;
import static org.openqa.selenium.remote.DriverCommand.QUIT;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.logging.profiler.HttpProfilerLogEntry;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpSessionId;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.JsonHttpCommandCodec;
import org.openqa.selenium.remote.http.JsonHttpResponseCodec;
import org.openqa.selenium.remote.internal.ApacheHttpClient;

import com.google.common.collect.ImmutableMap;

/**
 * BasilCommandExecutor is exactly the org.openqa.selenium.remote.HttpCommandExecutor.
 * It uses reflection to access some of the package access field/method for BasilDriver:
 * <ul>
 * <li>CommandInfo in defineCommand(String commandName, CommandInfo info)</li>
 * </ul>
 *
 * @author ryan131
 * @since Dec 21, 2016, 10:57:36 PM
 */
public class BasilCommandExecutor implements CommandExecutor, NeedsLocalLogs {

  private static HttpClient.Factory defaultClientFactory;

  private final URL remoteServer;
  private final HttpClient client;
  private final JsonHttpCommandCodec commandCodec;
  private final JsonHttpResponseCodec responseCodec;

  private LocalLogs logs = LocalLogs.getNullLogger();

  public BasilCommandExecutor(URL addressOfRemoteServer) {
    this(ImmutableMap.<String, CommandInfo>of(), addressOfRemoteServer);
  }

  /**
   * Creates an {@link BasilCommandExecutor} that supports non-standard
   * {@code additionalCommands} in addition to the standard.
   *
   * @param additionalCommands additional commands to allow the command executor to process
   * @param addressOfRemoteServer URL of remote end Selenium server
   */
  public BasilCommandExecutor(Map<String, CommandInfo> additionalCommands, URL addressOfRemoteServer) {
    this(additionalCommands, addressOfRemoteServer, getDefaultClientFactory());
  }

  public BasilCommandExecutor(Map<String, CommandInfo> additionalCommands, URL addressOfRemoteServer,
    HttpClient.Factory httpClientFactory) {

    try {
      remoteServer = addressOfRemoteServer == null
          ? new URL(System.getProperty("webdriver.remote.server", "http://localhost:4444/wd/hub"))
          : addressOfRemoteServer;
    } catch (MalformedURLException mue) {
      throw new WebDriverException(mue);
    }

    commandCodec = new JsonHttpCommandCodec();
    responseCodec = new JsonHttpResponseCodec();
    client = httpClientFactory.createClient(remoteServer);

    for (Map.Entry<String, CommandInfo> entry : additionalCommands.entrySet()) {
      defineCommand(entry.getKey(), entry.getValue());
      }
  }

  private static synchronized HttpClient.Factory getDefaultClientFactory() {
    if (defaultClientFactory == null) {
      defaultClientFactory = new ApacheHttpClient.Factory();
    }
    return defaultClientFactory;
  }

  /**
   * It may be useful to extend the commands understood by this {@code HttpCommandExecutor} at run
   * time, and this can be achieved via this method. Note, this is protected, and expected usage is
   * for subclasses only to call this.
   *
   * @param commandName The name of the command to use.
   * @param info CommandInfo for the command name provided
   */
  protected void defineCommand(String commandName, CommandInfo info) {
    checkNotNull(commandName);
    checkNotNull(info);

    // Use reflect to access

    try {
      Method infoGetMethod = CommandInfo.class.getMethod("getMethod", HttpMethod.class);
      infoGetMethod.setAccessible(true);
      HttpMethod httpMethod = (HttpMethod) infoGetMethod.invoke(this);

      Method infoGetUrl = CommandInfo.class.getMethod("getUrl", String.class);
      infoGetUrl.setAccessible(true);
      String url = (String) infoGetUrl.invoke(this);

      commandCodec.defineCommand(commandName, httpMethod, url);
    } catch (NoSuchMethodException nsme) {} catch (SecurityException se) {
    } catch (IllegalAccessException iae) {} catch (IllegalArgumentException iae) {
    } catch (InvocationTargetException ite) {}
  }

  public void setLocalLogs(LocalLogs logs) {
    this.logs = logs;
  }

  private void log(String logType, LogEntry entry) {
    logs.addEntry(logType, entry);
  }

  public URL getAddressOfRemoteServer() {
    return remoteServer;
  }

  // Code formatted and enhanced

  public Response execute(Command command) throws IOException {
    String commandName = command.getName();

    if (command.getSessionId() == null) {
      if (commandName.equals(QUIT)) {
        return new Response();
      }
      if (!commandName.equals(NEW_SESSION) && !commandName.equals(GET_ALL_SESSIONS)) {
        throw new NoSuchSessionException("Session ID is null. Using WebDriver after calling quit()?");
      }
    }

    HttpRequest httpRequest = commandCodec.encode(command);
    try {
      log(LogType.PROFILER, new HttpProfilerLogEntry(commandName, true));
      HttpResponse httpResponse = client.execute(httpRequest, true);
      log(LogType.PROFILER, new HttpProfilerLogEntry(commandName, false));

      Response response = responseCodec.decode(httpResponse);
      if (response.getSessionId() == null && httpResponse.getTargetHost() != null) {
        String sessionId = HttpSessionId.getSessionId(httpResponse.getTargetHost());
        response.setSessionId(sessionId);
      }
      if (commandName.equals(QUIT)) {
        client.close();
      }
      return response;
    } catch (UnsupportedCommandException uce) {
      if (uce.getMessage() == null || uce.getMessage().equals("")) {
        throw new UnsupportedOperationException(
            "No information from server. Command name was: " + commandName, uce.getCause());
      }
      throw uce;
    }
  }

}
