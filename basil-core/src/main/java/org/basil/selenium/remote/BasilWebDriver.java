/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingHandler;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.ErrorHandler;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteKeyboard;
import org.openqa.selenium.remote.RemoteLogs;
import org.openqa.selenium.remote.RemoteMouse;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.remote.UselessFileDetector;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;
import org.spearmint.Spearmint;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * <code>BasilWebDriver</code> is a copy of <code>RemoteWebDriver</code>.
 *
 * @author ryan131
 * @since Oct 28, 2015, 8:28:52 PM
 */
@SuppressWarnings({"deprecation", "unchecked", "unused"})
@Augmentable
public class BasilWebDriver extends RemoteWebDriver implements WebDriver, JavascriptExecutor,
    FindsById, FindsByClassName, FindsByLinkText, FindsByName,
    FindsByCssSelector, FindsByTagName, FindsByXPath,
    HasInputDevices, HasCapabilities, TakesScreenshot {

  // TODO(dawagner): This static logger should be unified with the per-instance localLogs
  private static final Logger logger = Logger.getLogger(BasilWebDriver.class.getName());
  private Level level = Level.FINE;

  private ErrorHandler errorHandler = new ErrorHandler();
  //TODO(ryan131): executor, capabilities, sessionId, converter, and localLogs were private.
  protected CommandExecutor executor;
  protected Capabilities capabilities;
  protected SessionId sessionId;
  private FileDetector fileDetector = new UselessFileDetector();
  private ExecuteMethod executeMethod;

  protected JsonToWebElementConverter converter;

  private RemoteKeyboard keyboard;
  private RemoteMouse mouse;
  private Logs remoteLogs;
  protected LocalLogs localLogs;

  // For cglib
  protected BasilWebDriver() {
    init(new DesiredCapabilities(), null);
  }

  public BasilWebDriver(CommandExecutor executor, Capabilities desiredCapabilities,
      Capabilities requiredCapabilities) {
    this.executor = executor;

    init(desiredCapabilities, requiredCapabilities);

    if (executor instanceof NeedsLocalLogs) {
      ((NeedsLocalLogs)executor).setLocalLogs(localLogs);
    }

    try {
      startClient();
    } catch (RuntimeException e) {
      try {
        stopClient();
      } catch (Exception ignored) {
        // Ignore the clean-up exception. We'll propagate the original failure.
      }

      throw e;
    }

    try {
      startSession(desiredCapabilities, requiredCapabilities);
    } catch (RuntimeException e) {
      try {
        quit();
      } catch (Exception ignored) {
        // Ignore the clean-up exception. We'll propagate the original failure.
      }

      throw e;
    }
  }

  public BasilWebDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
    this(executor, desiredCapabilities, null);
  }

  public BasilWebDriver(Capabilities desiredCapabilities) {
    this((URL) null, desiredCapabilities);
  }

  public BasilWebDriver(URL remoteAddress, Capabilities desiredCapabilities,
      Capabilities requiredCapabilities) {
    this(new BasilCommandExecutor(remoteAddress), desiredCapabilities,
        requiredCapabilities);
  }

  public BasilWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
    this(new BasilCommandExecutor(remoteAddress), desiredCapabilities, null);
  }

  // TODO(ryan131): This was private.
  protected void init(Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
    logger.addHandler(LoggingHandler.getInstance());

    converter = new JsonToWebElementConverter(this);
    executeMethod = new RemoteExecuteMethod(this);
    keyboard = new RemoteKeyboard(executeMethod);
    mouse = new RemoteMouse(executeMethod);

    ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<String>();

    boolean isProfilingEnabled = desiredCapabilities != null &&
        desiredCapabilities.is(CapabilityType.ENABLE_PROFILING_CAPABILITY);
    if (requiredCapabilities != null && requiredCapabilities.getCapability(
        CapabilityType.ENABLE_PROFILING_CAPABILITY) != null) {
      isProfilingEnabled = requiredCapabilities.is(CapabilityType.ENABLE_PROFILING_CAPABILITY);
    }
    if (isProfilingEnabled) {
      builder.add(LogType.PROFILER);
    }

    LoggingPreferences mergedLoggingPrefs = new LoggingPreferences();
    if (desiredCapabilities != null) {
      mergedLoggingPrefs.addPreferences((LoggingPreferences)desiredCapabilities.getCapability(
          CapabilityType.LOGGING_PREFS));
    }
    if (requiredCapabilities != null) {
      mergedLoggingPrefs.addPreferences((LoggingPreferences)requiredCapabilities.getCapability(
          CapabilityType.LOGGING_PREFS));
    }
    if ((mergedLoggingPrefs.getEnabledLogTypes().contains(LogType.CLIENT) &&
        mergedLoggingPrefs.getLevel(LogType.CLIENT) != Level.OFF) ||
        !mergedLoggingPrefs.getEnabledLogTypes().contains(LogType.CLIENT)) {
      builder.add(LogType.CLIENT);
    }

    Set<String> logTypesToInclude = builder.build();

    LocalLogs performanceLogger = LocalLogs.getStoringLoggerInstance(logTypesToInclude);
    LocalLogs clientLogs = LocalLogs.getHandlerBasedLoggerInstance(LoggingHandler.getInstance(),
        logTypesToInclude);
    localLogs = LocalLogs.getCombinedLogsHolder(clientLogs, performanceLogger);
    remoteLogs = new RemoteLogs(executeMethod, localLogs);
  }

  public SessionId getSessionId() {
    return sessionId;
  }

  protected void setSessionId(String opaqueKey) {
    sessionId = new SessionId(opaqueKey);
  }

  protected void startSession(Capabilities desiredCapabilities) {
    startSession(desiredCapabilities, null);
  }

  /**
   * Method called before {@link #startSession(Capabilities) starting a new session}. The default
   * implementation is a no-op, but subtypes should override this method to define custom behavior.
   */
  protected void startClient() {
  }

  /**
   * Method called after executing a {@link #quit()} command. Subtypes
   */
  protected void stopClient() {
  }

  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }

  public void setErrorHandler(ErrorHandler handler) {
    this.errorHandler = handler;
  }

  public CommandExecutor getCommandExecutor() {
    return executor;
  }

  protected void setCommandExecutor(CommandExecutor executor) {
    this.executor = executor;
  }

  public Capabilities getCapabilities() {
    return capabilities;
  }

  public List<WebElement> findElements(By by) {
    return by.findElements(this);
  }

  public WebElement findElement(By by) {
    return by.findElement(this);
  }

  protected WebElement findElement(String by, String using) {
    if (using == null) {
      throw new IllegalArgumentException("Cannot find elements when the selector is null.");
    }

    Response response = execute(DriverCommand.FIND_ELEMENT,
        ImmutableMap.of("using", by, "value", using));
    Object value = response.getValue();
    WebElement element;
    try {
      element = (WebElement) value;
    } catch (ClassCastException ex) {
      throw new WebDriverException("Returned value cannot be converted to WebElement: " + value, ex);
    }
    setFoundBy(this, element, by, using);
    return element;
  }

  @Override
  protected void setFoundBy(SearchContext context, WebElement element, String by, String using) {
    if (element instanceof RemoteWebElement) {
      RemoteWebElement remoteElement = ((RemoteWebElement) element);
      String foundBy = String.format("[%s] -> %s: %s", context, by, using);
      Spearmint.reflect(remoteElement).set("foundBy", foundBy);
    }
  }

  protected List<WebElement> findElements(String by, String using) {
    if (using == null) {
      throw new IllegalArgumentException("Cannot find elements when the selector is null.");
    }

    Response response = execute(DriverCommand.FIND_ELEMENTS,
        ImmutableMap.of("using", by, "value", using));
    Object value = response.getValue();
    List<WebElement> allElements;
    try {
      allElements = (List<WebElement>) value;
    } catch (ClassCastException ex) {
      throw new WebDriverException("Returned value cannot be converted to List<WebElement>: " + value, ex);
    }
    for (WebElement element : allElements) {
      setFoundBy(this, element, by, using);
    }
    return allElements;
  }

  public WebElement findElementById(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElement("id", using);
    } else {
      return findElementByCssSelector("#" + cssEscape(using));
    }
  }

  public List<WebElement> findElementsById(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElements("id", using);
    } else {
      return findElementsByCssSelector("#" + cssEscape(using));
    }
  }

  public WebElement findElementByLinkText(String using) {
    return findElement("link text", using);
  }

  public List<WebElement> findElementsByLinkText(String using) {
    return findElements("link text", using);
  }

  public WebElement findElementByPartialLinkText(String using) {
    return findElement("partial link text", using);
  }

  public List<WebElement> findElementsByPartialLinkText(String using) {
    return findElements("partial link text", using);
  }

  public WebElement findElementByTagName(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElement("tag name", using);
    } else {
      return findElementByCssSelector(using);
    }
  }

  public List<WebElement> findElementsByTagName(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElements("tag name", using);
    } else {
      return findElementsByCssSelector(using);
    }
  }

  public WebElement findElementByName(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElement("name", using);
    } else {
      return findElementByCssSelector("*[name='" + using + "']");
    }
  }

  public List<WebElement> findElementsByName(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElements("name", using);
    } else {
      return findElementsByCssSelector("*[name='" + using + "']");
    }
  }

  public WebElement findElementByClassName(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElement("class name", using);
    } else {
      return findElementByCssSelector("." + cssEscape(using));
    }
  }

  public List<WebElement> findElementsByClassName(String using) {
    if (getW3CStandardComplianceLevel() == 0) {
      return findElements("class name", using);
    } else {
      return findElementsByCssSelector("." + cssEscape(using));
    }
  }

  public WebElement findElementByCssSelector(String using) {
    return findElement("css selector", using);
  }

  public List<WebElement> findElementsByCssSelector(String using) {
    return findElements("css selector", using);
  }

  public WebElement findElementByXPath(String using) {
    return findElement("xpath", using);
  }

  public List<WebElement> findElementsByXPath(String using) {
    return findElements("xpath", using);
  }

  // Misc

  public Object executeScript(String script, Object... args) {
    if (!capabilities.isJavascriptEnabled()) {
      throw new UnsupportedOperationException(
          "You must be using an underlying instance of WebDriver that supports executing javascript");
    }

    // Escape the quote marks
    script = script.replaceAll("\"", "\\\"");

    Iterable<Object> convertedArgs = Iterables.transform(
        Lists.newArrayList(args), new WebElementToJsonConverter());

    Map<String, ?> params = ImmutableMap.of(
        "script", script,
        "args", Lists.newArrayList(convertedArgs));

    if (getW3CStandardComplianceLevel() > 0) {
      return execute(DriverCommand.EXECUTE_SCRIPT_W3C, params).getValue();
    }
    return execute(DriverCommand.EXECUTE_SCRIPT, params).getValue();
  }

  public Object executeAsyncScript(String script, Object... args) {
    if (!isJavascriptEnabled()) {
      throw new UnsupportedOperationException("You must be using an underlying instance of " +
          "WebDriver that supports executing javascript");
    }

    // Escape the quote marks
    script = script.replaceAll("\"", "\\\"");

    Iterable<Object> convertedArgs = Iterables.transform(
        Lists.newArrayList(args), new WebElementToJsonConverter());

    Map<String, ?> params = ImmutableMap.of(
        "script", script, "args", Lists.newArrayList(convertedArgs));

    if (getW3CStandardComplianceLevel() > 0) {
      return execute(DriverCommand.EXECUTE_ASYNC_SCRIPT_W3C, params).getValue();
    }
    return execute(DriverCommand.EXECUTE_ASYNC_SCRIPT, params).getValue();
  }

  protected Response execute(String driverCommand, Map<String, ?> parameters) {
    Command command = new Command(sessionId, driverCommand, parameters);
    Response response;

    long start = System.currentTimeMillis();
    String currentName = Thread.currentThread().getName();
    Thread.currentThread().setName(
        String.format("Forwarding %s on session %s to remote", driverCommand, sessionId));
    try {
      log(sessionId, command.getName(), command, When.BEFORE);
      response = executor.execute(command);
      log(sessionId, command.getName(), command, When.AFTER);

      if (response == null) {
        return null;
      }

      // Unwrap the response value by converting any JSON objects of the form
      // {"ELEMENT": id} to RemoteWebElements.
      Object value = converter.apply(response.getValue());
      response.setValue(value);
    } catch (NoSuchSessionException e){
      throw e;
    } catch (Exception e) {
      log(sessionId, command.getName(), command, When.EXCEPTION);
      String errorMessage = "Error communicating with the remote browser. " +
          "It may have died.";
      if (driverCommand.equals(DriverCommand.NEW_SESSION)) {
        errorMessage = "Could not start a new session. Possible causes are " +
            "invalid address of the remote server or browser start-up failure.";
      }
      UnreachableBrowserException ube = new UnreachableBrowserException(errorMessage, e);
      if (getSessionId() != null) {
        ube.addInfo(WebDriverException.SESSION_ID, getSessionId().toString());
      }
      if (getCapabilities() != null) {
        ube.addInfo("Capabilities", getCapabilities().toString());
      }
      throw ube;
    } finally {
      Thread.currentThread().setName(currentName);
    }

    try {
      errorHandler.throwIfResponseFailed(response, System.currentTimeMillis() - start);
    } catch (WebDriverException ex) {
      if (parameters != null && parameters.containsKey("using") && parameters.containsKey("value")) {
        ex.addInfo(
            "*** Element info",
            String.format(
                "{Using=%s, value=%s}",
                parameters.get("using"),
                parameters.get("value")));
      }
      ex.addInfo(WebDriverException.DRIVER_INFO, this.getClass().getName());
      if (getSessionId() != null) {
        ex.addInfo(WebDriverException.SESSION_ID, getSessionId().toString());
      }
      if (getCapabilities() != null) {
        ex.addInfo("Capabilities", getCapabilities().toString());
      }
      Throwables.propagate(ex);
    }
    return response;
  }

  protected Response execute(String command) {
    return execute(command, ImmutableMap.<String, Object>of());
  }

  public Keyboard getKeyboard() {
    return keyboard;
  }

  public Mouse getMouse() {
    return mouse;
  }

  /**
   * Session-reusable BasilWebDriver
   *
   * @author ryan131
   * @since Oct 28, 2015, 7:14:44 PM
   */
  public static class Reusable extends BasilWebDriver {

    private File localSessionFile;
    private SessionId localSessionId;
    private Capabilities localSession;
    private Map<SessionId, Capabilities> remoteSessions;

    // Constructors

    public Reusable(Capabilities desiredCapabilities) {
      this((URL) null, desiredCapabilities);
    }

    public Reusable(URL remoteAddress, Capabilities desiredCapabilities) {
      this(new BasilCommandExecutor(remoteAddress), desiredCapabilities, null);
    }

    public Reusable(URL remoteAddress, Capabilities desiredCapabilities,
        Capabilities requiredCapabilities) {
      this(new BasilCommandExecutor(remoteAddress), desiredCapabilities,
          requiredCapabilities);
    }

    public Reusable(CommandExecutor executor, Capabilities desiredCapabilities) {
      this(executor, desiredCapabilities, null);
    }

    public Reusable(CommandExecutor executor, Capabilities desiredCapabilities,
        Capabilities requiredCapabilities) {
      super.executor = executor;

      //String localSessionPath =
      //    System.getProperty("user.home") + "\\Selenium Remote Sessions\\";
      //localSessionFile =
      //    new File(localSessionPath + new Timestamp(new Date().getTime()));

      localSessionFile = new File("D:\\Selenium_Session");
      localSession = readSessionFromFile(localSessionFile);
      if (localSession != null) {
        localSessionId = getSessionId(localSession);
      }
      remoteSessions = getRemoteSessions();

      init(desiredCapabilities, requiredCapabilities);

      if (executor instanceof NeedsLocalLogs) {
        ((NeedsLocalLogs) executor).setLocalLogs(localLogs);
      }

      try {
        startClient();
      } catch (RuntimeException e) {
        try {
          stopClient();
        } catch (Exception ignored) {}

        throw e;
      }

      try {
        startSession(desiredCapabilities, requiredCapabilities);
      } catch (RuntimeException e) {
        try {
          quit();
        } catch (Exception ignored) {}

        throw e;
      }
    }

    // Assistive methods

    /**
     * A method provides a fast-hand for retrieve session ID from a
     * Capabilities object.
     * 
     * @param capabilities to get session Id from
     * @return the session ID of the passed in Capabilities
     */
    private SessionId getSessionId(Capabilities capabilities) {
      String key = "webdriver.remote.sessionid";
      String value = (String) capabilities.getCapability(key);
      return new SessionId(value);
    }

    /**
     * Sets the session ID of the specified Capabilities object.
     * 
     * @param capabilities to set session ID
     * @param sessionId to set to the capabilities
     */
    private void setSessionId(Capabilities capabilities, String sessionId) {
      DesiredCapabilities session = (DesiredCapabilities) capabilities;
      session.setCapability("webdriver.remote.sessionid", sessionId);
    }

    /**
     * A method for retrieving the sessions from remote. The session ID
     * is used as the key of the Map object whereas the Capabilities (session)
     * is the value.
     * 
     * @return a Map contains remote sessions
     */
    private Map<SessionId, Capabilities> getRemoteSessions() {
      Response response = execute(DriverCommand.GET_ALL_SESSIONS);
      List<Map<String, Object>> sessionList =
          (List<Map<String, Object>>) response.getValue();

      // For the ease of use, only useful information (ID and Capabilities)
      // are extracted from List<Map<String, Capabilities>> and stored in a
      // Map<String, Capabilities> object

      Map<SessionId, Capabilities> remoteSessions =
          new HashMap<SessionId, Capabilities>();

      for (Map<String, Object> session : sessionList) {
        String id = (String) session.get("id"); // Extract ID

        Map<String, Object> rawCapabilities =   // Extract Capabilities
            (Map<String, Object>) session.get("capabilities");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        for (Map.Entry<String, Object> entry : rawCapabilities.entrySet()) {
          capabilities.setCapability(entry.getKey(), entry.getValue());
        }

        setSessionId(capabilities, id);         // Set session ID
        // since returned capabilities doesn't contain session ID

        SessionId sId = new SessionId(id);
        remoteSessions.put(sId, capabilities);  // Store in session map
      }

      return remoteSessions;
    }

    /**
     * Reads a Capabilities object from specified source
     * 
     * @param input the source to read from
     * @return a Capabilities object
     */
    private Capabilities readSession(InputStream input) {
      Capabilities session = null;

      try (ObjectInputStream reader = new ObjectInputStream(input)) {
        // chrome issue
//        Object rawSession = reader.readObject();
//        if (rawSession instanceof Capabilities) {
//          session = (Capabilities) rawSession;
//        }

        Object rawMap = reader.readObject();
        if (!(rawMap instanceof HashMap)) {
          throw new ClassNotFoundException();
        }
        HashMap<String, Object> map = (HashMap<String, Object>) rawMap;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        for (Entry<String, Object> entry : map.entrySet()) {
          capabilities.setCapability(entry.getKey(), entry.getValue());
        }
        session = capabilities;
      } catch (IOException ioe) {
//        System.err.println("SessionReader has encountered an IOException!");
//        System.err.println("[Cause] " + ioe.getCause());
//        System.err.println("[Message] " + ioe.getMessage());
      } catch (ClassNotFoundException cnfe) {
        System.err.println("The session file may have been corrupted.");
      }

      return session;
    }

    private Capabilities readSessionFromFile(File sessionFile) {
      Capabilities session = null;
      InputStream source = null;

      try {
        source = new BufferedInputStream(new FileInputStream(sessionFile));
        session = readSession(source);
      } catch (FileNotFoundException e) {
      } finally {
        if (source != null) {
          try {
            source.close();
          } catch (IOException ignored) {}
        }
      }

      return session;
    }

    /**
     * Saves the session to specified storage
     * 
     * @param session to save
     * @param output the location to save the session
     * @return true if the session is saved successful
     */
    private boolean saveSession(Capabilities session, OutputStream output) {
      boolean saved = false;

      try (
          ObjectOutputStream writer = new ObjectOutputStream(output);
      ) {
        // chrome issue
//        writer.writeObject(session);
        Map<String, Object> sessionMap = new HashMap<String, Object>();
        System.err.println("[BasilWebDriver.Reusable>saveSession] session == null: " + (session == null));
        for (Entry<String, ?> entry : session.asMap().entrySet()) {
          sessionMap.put(entry.getKey(), entry.getValue());
        }
        writer.writeObject(sessionMap);
        saved = true;
      } catch (IOException ioe) {
//        System.err.println("SessionWriter has encountered an IOException!");
//        System.err.println("[Cause] " + ioe.getCause());
//        System.err.println("[Message] " + ioe.getMessage());
        saved = false;
      }

      return saved;
    }

    private boolean saveSessionToFile(Capabilities session, File file) {
      boolean saved = false;
      BufferedOutputStream target = null;

      try {
        target = new BufferedOutputStream(new FileOutputStream(file));
        saved = saveSession(session, target);
      } catch (FileNotFoundException e) {
      } finally {
        if (target != null) {
          try {
            target.close();
          } catch (IOException ignored) {}
        }
      }

      return saved;
    }

    private boolean deleteSessionFile(File sessionFile) {
      return sessionFile.delete();
    }

    private void reuseSession(Capabilities previousSession) {
      super.capabilities = previousSession;
      super.sessionId = getSessionId(previousSession);
    }

    // Overridden superclass methods

    @Override
    protected void startSession(
      Capabilities desiredCapabilities, Capabilities requiredCapabilities) {

      // Best scenario: There is a match between a local session and a remote
      // session

      if (remoteSessions.containsKey(localSessionId)) {
        String message = "Previous session: \"" + localSessionId + "\""
            + " found.\nContinuing tests with the existing session.";
        System.err.println(message);

        reuseSession(localSession);

        return;
      }

      // There isn't a match between the local session and any remote session

      if (localSession != null) {
        String message =  "The previous session \"" + localSessionId
            + "\" has expired.";
        System.err.println(message);

        deleteSessionFile(localSessionFile);
      }

      if (remoteSessions.isEmpty()) {
        // Create and save the new session when remote has no sessions
        super.startSession(desiredCapabilities, requiredCapabilities);
        saveSessionToFile(super.capabilities, localSessionFile);

        String message = "The newly-created session: \"" + super.sessionId
            +"\" is saved to \"" + localSessionFile + "\".";
        System.err.println(message);
      } else {
        System.err.println("/------------------------------------------------------------------------------/");
        System.err.println("/----------------- Session-reusable RemoteWebDriver by Li Wan -----------------/");
        System.err.println("/---------------------------- randyryan@gmail.com -----------------------------/");
        System.err.println("/------------------------------------------------------------------------------/");
        System.err.println("Continuing tests in:\n");
        // Allow user to select a session from remote and save locally
        int sessionOrder = 1;
        for (Entry<SessionId, Capabilities> entry : remoteSessions.entrySet()) {
          System.err.printf("[Session #%d]%n", sessionOrder);
          System.err.println("Browser: " + entry.getValue().getBrowserName());
          System.err.println("Session: " + entry.getKey());
          System.err.println("Webpage: " + getSessionInfo(entry.getKey(), DriverCommand.GET_TITLE));
          System.err.println("Address: " + getSessionInfo(entry.getKey(), DriverCommand.GET_CURRENT_URL));

          sessionOrder++;

          super.sessionId = entry.getKey();
          super.capabilities = entry.getValue();
        }
        System.err.println("/------------------------------------------------------------------------------/");
        System.err.println("/---------------------------- Happy debugging! :D -----------------------------/");
        System.err.println("/------------------------------------------------------------------------------/");

        saveSessionToFile(super.capabilities, localSessionFile);
      }
    }

    private String getSessionInfo(SessionId sessionId, String driverCommand) {
      Command command = new Command(sessionId, driverCommand);

      Response response = null;
      try {
        response = executor.execute(command);
      } catch (IOException ioe) {}

      Object value = null;
      if (response != null) {
        value = converter.apply(response.getValue());
      }

      if (value == null) {
        String message = "WebDriver failed to retrieve \"" + driverCommand
            + "\" information for the session \"" + sessionId + "\"!";
        throw new WebDriverException(message);
      }

      return value.toString();
    }

  }

}
