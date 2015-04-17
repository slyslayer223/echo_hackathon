package session;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

public class PizzaSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(SessionSpeechlet.class);

    private static final String TOPPING_ONE_KEY = "TOPPINGONE";
    private static final String TOPPING_ONE_SLOT = "ToppingOne";

    private static final String TOPPING_TWO_KEY = "TOPPINGTWO";
    private static final String TOPPING_TWO_SLOT = "ToppingTwo";

    private static final String TOPPING_THREE_KEY = "TOPPINGTHREE";
    private static final String TOPPING_THREE_SLOT = "ToppingThree";

    private boolean FINAL_STATE = false;

    // Pizza Tracking States

    private static final String PREPARED_STATE = "PREPARED_STATE";
    private static final String OVEN_STATE = "OVEN_STATE";
    private static final String READY_STATE = "READY_STATE";
    private static final String ON_WAY_STATE = "ON_WAY_STATE";

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // Get intent from the request object.
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // Note: If the session is started with an intent, no welcome message will be rendered;
        // rather, the intent specific response will be returned.
        if ("IWantPizzaIntent".equals(intentName)) {
            return iWantPizza(intent, session);
        } else if ("TrackerIntent".equals(intentName)) {
            return trackPizza(intent, session);
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    private SpeechletResponse iWantPizza(final Intent intent, final Session session) {

        String speechOutput = "What kind?";
        boolean shouldEndSession = false;

        if (FINAL_STATE) {
            FINAL_STATE = false;

            speechOutput = "Ok you want a cheese pizza should I place the order or edit it";
            shouldEndSession = true;
        } else {
            FINAL_STATE = true;
        }
        
        return buildSpeechletResponse(intent.getName(), speechOutput, shouldEndSession);
    }

    private SpeechletResponse trackPizza(final Intent intent, final Session session) {

        String speechOutput = "";
        boolean shouldEndSession = true;

        String pizzaState = getPizzaState();

        if (pizzaState.equals(PREPARED_STATE)) {
            speechOutput = "your pizza is being prepared";
        } else if (pizzaState.equals(OVEN_STATE)) {
            speechOutput = "your pizza is in the oven";
        } else if (pizzaState.equals(READY_STATE)) {
            speechOutput = "your pizza is ready";
        } else if (pizzaState.equals(ON_WAY_STATE)) {
            speechOutput = "your pizza is on the way";
        }

        return buildSpeechletResponse(intent.getName(), speechOutput, shouldEndSession);
    }

    /**
     * Creates and returns the visual and spoken response with shouldEndSession flag
     * 
     * @param title
     *            title for the companion application home card
     * @param output
     *            output content for speech and companion application home card
     * @param shouldEndSession
     *            should the session be closed
     * @return SpeechletResponse spoken and visual response for the given input
     */
    private SpeechletResponse buildSpeechletResponse(final String title, final String output,
            final boolean shouldEndSession) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(String.format("SessionSpeechlet - %s", title));
        card.setSubtitle(String.format("SessionSpeechlet - Sub Title"));
        card.setContent(String.format("SessionSpeechlet - %s", output));

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(output);

        // Create the speechlet response.
        SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(shouldEndSession);
        response.setOutputSpeech(speech);
        response.setCard(card);
        return response;
    }

    private String getPizzaState() {
        String userPhoneNumber = ""; // Get from user prefs

        // Make api call to dominos

        return PREPARED_STATE;
    }
}
