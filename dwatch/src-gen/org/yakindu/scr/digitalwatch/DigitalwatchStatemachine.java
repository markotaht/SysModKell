package org.yakindu.scr.digitalwatch;
import org.yakindu.scr.ITimer;

public class DigitalwatchStatemachine implements IDigitalwatchStatemachine {

	protected class SCIButtonsImpl implements SCIButtons {
	
		private boolean topLeftPressed;
		
		public void raiseTopLeftPressed() {
			topLeftPressed = true;
		}
		
		private boolean topLeftReleased;
		
		public void raiseTopLeftReleased() {
			topLeftReleased = true;
		}
		
		private boolean topRightPressed;
		
		public void raiseTopRightPressed() {
			topRightPressed = true;
		}
		
		private boolean topRightReleased;
		
		public void raiseTopRightReleased() {
			topRightReleased = true;
		}
		
		private boolean bottomLeftPressed;
		
		public void raiseBottomLeftPressed() {
			bottomLeftPressed = true;
		}
		
		private boolean bottomLeftReleased;
		
		public void raiseBottomLeftReleased() {
			bottomLeftReleased = true;
		}
		
		private boolean bottomRightPressed;
		
		public void raiseBottomRightPressed() {
			bottomRightPressed = true;
		}
		
		private boolean bottomRightReleased;
		
		public void raiseBottomRightReleased() {
			bottomRightReleased = true;
		}
		
		protected void clearEvents() {
			topLeftPressed = false;
			topLeftReleased = false;
			topRightPressed = false;
			topRightReleased = false;
			bottomLeftPressed = false;
			bottomLeftReleased = false;
			bottomRightPressed = false;
			bottomRightReleased = false;
		}
	}
	
	protected SCIButtonsImpl sCIButtons;
	
	protected class SCIDisplayImpl implements SCIDisplay {
	
		private SCIDisplayOperationCallback operationCallback;
		
		public void setSCIDisplayOperationCallback(
				SCIDisplayOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
	}
	
	protected SCIDisplayImpl sCIDisplay;
	
	protected class SCILogicUnitImpl implements SCILogicUnit {
	
		private SCILogicUnitOperationCallback operationCallback;
		
		public void setSCILogicUnitOperationCallback(
				SCILogicUnitOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
		private boolean startAlarm;
		
		public void raiseStartAlarm() {
			startAlarm = true;
		}
		
		protected void clearEvents() {
			startAlarm = false;
		}
	}
	
	protected SCILogicUnitImpl sCILogicUnit;
	
	protected class SCIInternalImpl implements SCIInternal {
	
		private boolean update;
		
		public void raiseUpdate() {
			update = true;
		}
		
		protected void clearEvents() {
			update = false;
		}
	}
	
	protected SCIInternalImpl sCIInternal;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_digitalwatch,
		main_region_digitalwatch_Ticker_Main,
		main_region_digitalwatch_Ticker_Main_Main_Placeholder,
		main_region_digitalwatch_Ticker_Main_Main_Temp,
		main_region_digitalwatch_Ticker_Main_Ticker_Tick,
		main_region_digitalwatch_Ticker_Main_Update_TimeDisplay,
		main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay,
		main_region_digitalwatch_Ticker_Editing,
		main_region_digitalwatch_Ticker_Editing_Editing_HEhe,
		main_region_digitalwatch_Light_LightOff,
		main_region_digitalwatch_Light_LightOn,
		main_region_digitalwatch_Light_Wait,
		$NullState$
	};
	
	private final State[] stateVector = new State[4];
	
	private int nextStateIndex;
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[4];
	public DigitalwatchStatemachine() {
		sCIButtons = new SCIButtonsImpl();
		sCIDisplay = new SCIDisplayImpl();
		sCILogicUnit = new SCILogicUnitImpl();
		sCIInternal = new SCIInternalImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		for (int i = 0; i < 4; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_main_region_default();
	}
	
	public void exit() {
		exitSequence_main_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$||stateVector[1] != State.$NullState$||stateVector[2] != State.$NullState$||stateVector[3] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCIButtons.clearEvents();
		sCILogicUnit.clearEvents();
		sCIInternal.clearEvents();
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_digitalwatch:
			return stateVector[0].ordinal() >= State.
					main_region_digitalwatch.ordinal()&& stateVector[0].ordinal() <= State.main_region_digitalwatch_Light_Wait.ordinal();
		case main_region_digitalwatch_Ticker_Main:
			return stateVector[0].ordinal() >= State.
					main_region_digitalwatch_Ticker_Main.ordinal()&& stateVector[0].ordinal() <= State.main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay.ordinal();
		case main_region_digitalwatch_Ticker_Main_Main_Placeholder:
			return stateVector[0] == State.main_region_digitalwatch_Ticker_Main_Main_Placeholder;
		case main_region_digitalwatch_Ticker_Main_Main_Temp:
			return stateVector[0] == State.main_region_digitalwatch_Ticker_Main_Main_Temp;
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			return stateVector[1] == State.main_region_digitalwatch_Ticker_Main_Ticker_Tick;
		case main_region_digitalwatch_Ticker_Main_Update_TimeDisplay:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_TimeDisplay;
		case main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay;
		case main_region_digitalwatch_Ticker_Editing:
			return stateVector[0].ordinal() >= State.
					main_region_digitalwatch_Ticker_Editing.ordinal()&& stateVector[0].ordinal() <= State.main_region_digitalwatch_Ticker_Editing_Editing_HEhe.ordinal();
		case main_region_digitalwatch_Ticker_Editing_Editing_HEhe:
			return stateVector[0] == State.main_region_digitalwatch_Ticker_Editing_Editing_HEhe;
		case main_region_digitalwatch_Light_LightOff:
			return stateVector[3] == State.main_region_digitalwatch_Light_LightOff;
		case main_region_digitalwatch_Light_LightOn:
			return stateVector[3] == State.main_region_digitalwatch_Light_LightOn;
		case main_region_digitalwatch_Light_Wait:
			return stateVector[3] == State.main_region_digitalwatch_Light_Wait;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correct
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
	}
	
	public SCIButtons getSCIButtons() {
		return sCIButtons;
	}
	
	public SCIDisplay getSCIDisplay() {
		return sCIDisplay;
	}
	
	public SCILogicUnit getSCILogicUnit() {
		return sCILogicUnit;
	}
	
	public SCIInternal getSCIInternal() {
		return sCIInternal;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Main_Placeholder_tr0_tr0() {
		return sCIButtons.bottomRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Main_Temp_tr0_tr0() {
		return sCIButtons.bottomRightReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Main_Temp_tr1_tr1() {
		return timeEvents[0];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Ticker_Tick_tr0_tr0() {
		return timeEvents[1];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr0_tr0() {
		return sCIInternal.update;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr1_tr1() {
		return sCIButtons.topLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr0_tr0() {
		return sCIInternal.update;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr1_tr1() {
		return sCIButtons.topLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_tr0_tr0() {
		return timeEvents[2];
	}
	
	private boolean check_main_region_digitalwatch_Light_LightOff_tr0_tr0() {
		return sCIButtons.topRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Light_LightOn_tr0_tr0() {
		return sCIButtons.topRightReleased;
	}
	
	private boolean check_main_region_digitalwatch_Light_Wait_tr0_tr0() {
		return timeEvents[3];
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Main_Placeholder_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder();
		enterSequence_main_region_digitalwatch_Ticker_Main_Main_Temp_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Main_Temp_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Main_Temp();
		enterSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Main_Temp_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main();
		enterSequence_main_region_digitalwatch_Ticker_Editing_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Ticker_Tick_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
		sCIInternal.raiseUpdate();
		
		enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing();
		enterSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_default();
	}
	
	private void effect_main_region_digitalwatch_Light_LightOff_tr0() {
		exitSequence_main_region_digitalwatch_Light_LightOff();
		enterSequence_main_region_digitalwatch_Light_LightOn_default();
	}
	
	private void effect_main_region_digitalwatch_Light_LightOn_tr0() {
		exitSequence_main_region_digitalwatch_Light_LightOn();
		enterSequence_main_region_digitalwatch_Light_Wait_default();
	}
	
	private void effect_main_region_digitalwatch_Light_Wait_tr0() {
		exitSequence_main_region_digitalwatch_Light_Wait();
		enterSequence_main_region_digitalwatch_Light_LightOff_default();
	}
	
	/* Entry action for state 'Temp'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Main_Temp() {
		timer.setTimer(this, 0, 1500, false);
	}
	
	/* Entry action for state 'Tick'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Ticker_Tick() {
		timer.setTimer(this, 1, 1*1000, false);
		
		sCILogicUnit.operationCallback.increaseTimeByOne();
	}
	
	/* Entry action for state 'TimeDisplay'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay() {
		sCIDisplay.operationCallback.refreshTimeDisplay();
		
		sCIDisplay.operationCallback.refreshDateDisplay();
	}
	
	/* Entry action for state 'ChronoDisplay'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay() {
		sCIDisplay.operationCallback.refreshChronoDisplay();
	}
	
	/* Entry action for state 'Editing'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing() {
		timer.setTimer(this, 2, 5*1000, false);
		
		sCILogicUnit.operationCallback.startTimeEditMode();
	}
	
	/* Entry action for state 'LightOn'. */
	private void entryAction_main_region_digitalwatch_Light_LightOn() {
		sCIDisplay.operationCallback.setIndiglo();
	}
	
	/* Entry action for state 'Wait'. */
	private void entryAction_main_region_digitalwatch_Light_Wait() {
		timer.setTimer(this, 3, 2*1000, false);
	}
	
	/* Exit action for state 'Temp'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Main_Temp() {
		timer.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'Tick'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Ticker_Tick() {
		timer.unsetTimer(this, 1);
	}
	
	/* Exit action for state 'Editing'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing() {
		timer.unsetTimer(this, 2);
	}
	
	/* Exit action for state 'Wait'. */
	private void exitAction_main_region_digitalwatch_Light_Wait() {
		timer.unsetTimer(this, 3);
		
		sCIDisplay.operationCallback.unsetIndiglo();
	}
	
	/* 'default' enter sequence for state digitalwatch */
	private void enterSequence_main_region_digitalwatch_default() {
		enterSequence_main_region_digitalwatch_Ticker_default();
		enterSequence_main_region_digitalwatch_Light_default();
	}
	
	/* 'default' enter sequence for state Main */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Main_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_default();
	}
	
	/* 'default' enter sequence for state Placeholder */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_digitalwatch_Ticker_Main_Main_Placeholder;
	}
	
	/* 'default' enter sequence for state Temp */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Main_Temp_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Main_Temp();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_digitalwatch_Ticker_Main_Main_Temp;
	}
	
	/* 'default' enter sequence for state Tick */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
		nextStateIndex = 1;
		stateVector[1] = State.main_region_digitalwatch_Ticker_Main_Ticker_Tick;
	}
	
	/* 'default' enter sequence for state TimeDisplay */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_TimeDisplay;
	}
	
	/* 'default' enter sequence for state ChronoDisplay */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay;
	}
	
	/* 'default' enter sequence for state Editing */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing();
		enterSequence_main_region_digitalwatch_Ticker_Editing_Editing_default();
	}
	
	/* 'default' enter sequence for state HEhe */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_Editing_HEhe_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_digitalwatch_Ticker_Editing_Editing_HEhe;
	}
	
	/* 'default' enter sequence for state LightOff */
	private void enterSequence_main_region_digitalwatch_Light_LightOff_default() {
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Light_LightOff;
	}
	
	/* 'default' enter sequence for state LightOn */
	private void enterSequence_main_region_digitalwatch_Light_LightOn_default() {
		entryAction_main_region_digitalwatch_Light_LightOn();
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Light_LightOn;
	}
	
	/* 'default' enter sequence for state Wait */
	private void enterSequence_main_region_digitalwatch_Light_Wait_default() {
		entryAction_main_region_digitalwatch_Light_Wait();
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Light_Wait;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* 'default' enter sequence for region Ticker */
	private void enterSequence_main_region_digitalwatch_Ticker_default() {
		react_main_region_digitalwatch_Ticker__entry_Default();
	}
	
	/* 'default' enter sequence for region Main */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Main_default() {
		react_main_region_digitalwatch_Ticker_Main_Main__entry_Default();
	}
	
	/* 'default' enter sequence for region Ticker */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_default() {
		react_main_region_digitalwatch_Ticker_Main_Ticker__entry_Default();
	}
	
	/* 'default' enter sequence for region Update */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_default() {
		react_main_region_digitalwatch_Ticker_Main_Update__entry_Default();
	}
	
	/* 'default' enter sequence for region Editing */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_Editing_default() {
		react_main_region_digitalwatch_Ticker_Editing_Editing__entry_Default();
	}
	
	/* 'default' enter sequence for region Light */
	private void enterSequence_main_region_digitalwatch_Light_default() {
		react_main_region_digitalwatch_Light__entry_Default();
	}
	
	/* Default exit sequence for state Main */
	private void exitSequence_main_region_digitalwatch_Ticker_Main() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Main();
		exitSequence_main_region_digitalwatch_Ticker_Main_Ticker();
		exitSequence_main_region_digitalwatch_Ticker_Main_Update();
	}
	
	/* Default exit sequence for state Placeholder */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Temp */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Main_Temp() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Main_Temp();
	}
	
	/* Default exit sequence for state Tick */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
	}
	
	/* Default exit sequence for state TimeDisplay */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
	}
	
	/* Default exit sequence for state ChronoDisplay */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
	}
	
	/* Default exit sequence for state Editing */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_Editing();
		exitAction_main_region_digitalwatch_Ticker_Editing();
	}
	
	/* Default exit sequence for state HEhe */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_Editing_HEhe() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state LightOff */
	private void exitSequence_main_region_digitalwatch_Light_LightOff() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
	}
	
	/* Default exit sequence for state LightOn */
	private void exitSequence_main_region_digitalwatch_Light_LightOn() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
	}
	
	/* Default exit sequence for state Wait */
	private void exitSequence_main_region_digitalwatch_Light_Wait() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Light_Wait();
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_digitalwatch_Ticker_Main_Main_Placeholder:
			exitSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder();
			break;
		case main_region_digitalwatch_Ticker_Main_Main_Temp:
			exitSequence_main_region_digitalwatch_Ticker_Main_Main_Temp();
			break;
		case main_region_digitalwatch_Ticker_Editing_Editing_HEhe:
			exitSequence_main_region_digitalwatch_Ticker_Editing_Editing_HEhe();
			exitAction_main_region_digitalwatch_Ticker_Editing();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
			break;
		default:
			break;
		}
		
		switch (stateVector[2]) {
		case main_region_digitalwatch_Ticker_Main_Update_TimeDisplay:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
			break;
		default:
			break;
		}
		
		switch (stateVector[3]) {
		case main_region_digitalwatch_Light_LightOff:
			exitSequence_main_region_digitalwatch_Light_LightOff();
			break;
		case main_region_digitalwatch_Light_LightOn:
			exitSequence_main_region_digitalwatch_Light_LightOn();
			break;
		case main_region_digitalwatch_Light_Wait:
			exitSequence_main_region_digitalwatch_Light_Wait();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Ticker */
	private void exitSequence_main_region_digitalwatch_Ticker() {
		switch (stateVector[0]) {
		case main_region_digitalwatch_Ticker_Main_Main_Placeholder:
			exitSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder();
			break;
		case main_region_digitalwatch_Ticker_Main_Main_Temp:
			exitSequence_main_region_digitalwatch_Ticker_Main_Main_Temp();
			break;
		case main_region_digitalwatch_Ticker_Editing_Editing_HEhe:
			exitSequence_main_region_digitalwatch_Ticker_Editing_Editing_HEhe();
			exitAction_main_region_digitalwatch_Ticker_Editing();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
			break;
		default:
			break;
		}
		
		switch (stateVector[2]) {
		case main_region_digitalwatch_Ticker_Main_Update_TimeDisplay:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Main */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Main() {
		switch (stateVector[0]) {
		case main_region_digitalwatch_Ticker_Main_Main_Placeholder:
			exitSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder();
			break;
		case main_region_digitalwatch_Ticker_Main_Main_Temp:
			exitSequence_main_region_digitalwatch_Ticker_Main_Main_Temp();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Ticker */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Ticker() {
		switch (stateVector[1]) {
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Update */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update() {
		switch (stateVector[2]) {
		case main_region_digitalwatch_Ticker_Main_Update_TimeDisplay:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Editing */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_Editing() {
		switch (stateVector[0]) {
		case main_region_digitalwatch_Ticker_Editing_Editing_HEhe:
			exitSequence_main_region_digitalwatch_Ticker_Editing_Editing_HEhe();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Light */
	private void exitSequence_main_region_digitalwatch_Light() {
		switch (stateVector[3]) {
		case main_region_digitalwatch_Light_LightOff:
			exitSequence_main_region_digitalwatch_Light_LightOff();
			break;
		case main_region_digitalwatch_Light_LightOn:
			exitSequence_main_region_digitalwatch_Light_LightOn();
			break;
		case main_region_digitalwatch_Light_Wait:
			exitSequence_main_region_digitalwatch_Light_Wait();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state Placeholder. */
	private void react_main_region_digitalwatch_Ticker_Main_Main_Placeholder() {
		if (check_main_region_digitalwatch_Ticker_Main_Main_Placeholder_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Main_Placeholder_tr0();
		}
	}
	
	/* The reactions of state Temp. */
	private void react_main_region_digitalwatch_Ticker_Main_Main_Temp() {
		if (check_main_region_digitalwatch_Ticker_Main_Main_Temp_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Main_Temp_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Main_Temp_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Main_Temp_tr1();
			}
		}
	}
	
	/* The reactions of state Tick. */
	private void react_main_region_digitalwatch_Ticker_Main_Ticker_Tick() {
		if (check_main_region_digitalwatch_Ticker_Main_Ticker_Tick_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Ticker_Tick_tr0();
		}
	}
	
	/* The reactions of state TimeDisplay. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr1();
			}
		}
	}
	
	/* The reactions of state ChronoDisplay. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr1();
			}
		}
	}
	
	/* The reactions of state HEhe. */
	private void react_main_region_digitalwatch_Ticker_Editing_Editing_HEhe() {
		if (check_main_region_digitalwatch_Ticker_Editing_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_tr0();
		} else {
		}
	}
	
	/* The reactions of state LightOff. */
	private void react_main_region_digitalwatch_Light_LightOff() {
		if (check_main_region_digitalwatch_Light_LightOff_tr0_tr0()) {
			effect_main_region_digitalwatch_Light_LightOff_tr0();
		}
	}
	
	/* The reactions of state LightOn. */
	private void react_main_region_digitalwatch_Light_LightOn() {
		if (check_main_region_digitalwatch_Light_LightOn_tr0_tr0()) {
			effect_main_region_digitalwatch_Light_LightOn_tr0();
		}
	}
	
	/* The reactions of state Wait. */
	private void react_main_region_digitalwatch_Light_Wait() {
		if (check_main_region_digitalwatch_Light_Wait_tr0_tr0()) {
			effect_main_region_digitalwatch_Light_Wait_tr0();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_digitalwatch_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Main_Main__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Main_Placeholder_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Main_Ticker__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Main_Update__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Editing_Editing__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Editing_Editing_HEhe_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Light__entry_Default() {
		enterSequence_main_region_digitalwatch_Light_LightOff_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_digitalwatch_Ticker_Main_Main_Placeholder:
				react_main_region_digitalwatch_Ticker_Main_Main_Placeholder();
				break;
			case main_region_digitalwatch_Ticker_Main_Main_Temp:
				react_main_region_digitalwatch_Ticker_Main_Main_Temp();
				break;
			case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
				react_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_TimeDisplay:
				react_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay:
				react_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
				break;
			case main_region_digitalwatch_Ticker_Editing_Editing_HEhe:
				react_main_region_digitalwatch_Ticker_Editing_Editing_HEhe();
				break;
			case main_region_digitalwatch_Light_LightOff:
				react_main_region_digitalwatch_Light_LightOff();
				break;
			case main_region_digitalwatch_Light_LightOn:
				react_main_region_digitalwatch_Light_LightOn();
				break;
			case main_region_digitalwatch_Light_Wait:
				react_main_region_digitalwatch_Light_Wait();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
