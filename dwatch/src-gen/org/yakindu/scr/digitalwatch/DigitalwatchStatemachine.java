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
		
		private boolean chronoUpdate;
		
		public void raiseChronoUpdate() {
			chronoUpdate = true;
		}
		
		protected void clearEvents() {
			update = false;
			chronoUpdate = false;
		}
	}
	
	protected SCIInternalImpl sCIInternal;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_digitalwatch,
		main_region_digitalwatch_ChronoTicker_Tick,
		main_region_digitalwatch_ChronoTicker_Stopped,
		main_region_digitalwatch_Ticker_Main,
		main_region_digitalwatch_Ticker_Main_Ticker_Tick,
		main_region_digitalwatch_Ticker_Main_Update_TimeDisplay,
		main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay,
		main_region_digitalwatch_Ticker_Main_Update_Resetting,
		main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle,
		main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset,
		main_region_digitalwatch_Ticker_Main_Update_toTimeEdit,
		main_region_digitalwatch_Ticker_Editing,
		main_region_digitalwatch_Ticker_Editing_increase_idle,
		main_region_digitalwatch_Ticker_Editing_increase_increaseSelection,
		main_region_digitalwatch_Ticker_Editing_changeSelection_idle,
		main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection,
		main_region_digitalwatch_Ticker_Editing_blinker_hideSelection,
		main_region_digitalwatch_Ticker_Editing_blinker_showSelection,
		main_region_digitalwatch_Ticker_Editing_idleController_idle,
		main_region_digitalwatch_Ticker_Editing_idleController_idleReset,
		main_region_digitalwatch_Light_LightOff,
		main_region_digitalwatch_Light_LightOn,
		main_region_digitalwatch_Light_Wait,
		main_region_digitalwatch_Alarm_idle,
		main_region_digitalwatch_Alarm_AlarmOn,
		main_region_digitalwatch_Alarm_ActivateAlarm,
		main_region_digitalwatch_Alarm_AlarmOff,
		main_region_digitalwatch_AlarmFlasher_idle,
		main_region_digitalwatch_AlarmFlasher_AlarmFlashOn,
		main_region_digitalwatch_AlarmFlasher_AlarmFlashOff,
		$NullState$
	};
	
	private final State[] stateVector = new State[9];
	
	private int nextStateIndex;
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[21];
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
		for (int i = 0; i < 9; i++) {
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
		return stateVector[0] != State.$NullState$||stateVector[1] != State.$NullState$||stateVector[2] != State.$NullState$||stateVector[3] != State.$NullState$||stateVector[4] != State.$NullState$||stateVector[5] != State.$NullState$||stateVector[6] != State.$NullState$||stateVector[7] != State.$NullState$||stateVector[8] != State.$NullState$;
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
					main_region_digitalwatch.ordinal()&& stateVector[0].ordinal() <= State.main_region_digitalwatch_AlarmFlasher_AlarmFlashOff.ordinal();
		case main_region_digitalwatch_ChronoTicker_Tick:
			return stateVector[0] == State.main_region_digitalwatch_ChronoTicker_Tick;
		case main_region_digitalwatch_ChronoTicker_Stopped:
			return stateVector[0] == State.main_region_digitalwatch_ChronoTicker_Stopped;
		case main_region_digitalwatch_Ticker_Main:
			return stateVector[1].ordinal() >= State.
					main_region_digitalwatch_Ticker_Main.ordinal()&& stateVector[1].ordinal() <= State.main_region_digitalwatch_Ticker_Main_Update_toTimeEdit.ordinal();
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			return stateVector[1] == State.main_region_digitalwatch_Ticker_Main_Ticker_Tick;
		case main_region_digitalwatch_Ticker_Main_Update_TimeDisplay:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_TimeDisplay;
		case main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay;
		case main_region_digitalwatch_Ticker_Main_Update_Resetting:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_Resetting;
		case main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing:
			return stateVector[2].ordinal() >= State.
					main_region_digitalwatch_Ticker_Main_Update_AlarmEditing.ordinal()&& stateVector[2].ordinal() <= State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset.ordinal();
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle:
			return stateVector[3] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection:
			return stateVector[3] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection:
			return stateVector[4] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection:
			return stateVector[4] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking:
			return stateVector[4] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle:
			return stateVector[5] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset:
			return stateVector[5] == State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset;
		case main_region_digitalwatch_Ticker_Main_Update_toTimeEdit:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Main_Update_toTimeEdit;
		case main_region_digitalwatch_Ticker_Editing:
			return stateVector[1].ordinal() >= State.
					main_region_digitalwatch_Ticker_Editing.ordinal()&& stateVector[1].ordinal() <= State.main_region_digitalwatch_Ticker_Editing_idleController_idleReset.ordinal();
		case main_region_digitalwatch_Ticker_Editing_increase_idle:
			return stateVector[1] == State.main_region_digitalwatch_Ticker_Editing_increase_idle;
		case main_region_digitalwatch_Ticker_Editing_increase_increaseSelection:
			return stateVector[1] == State.main_region_digitalwatch_Ticker_Editing_increase_increaseSelection;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_idle:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Editing_changeSelection_idle;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection:
			return stateVector[2] == State.main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection;
		case main_region_digitalwatch_Ticker_Editing_blinker_hideSelection:
			return stateVector[3] == State.main_region_digitalwatch_Ticker_Editing_blinker_hideSelection;
		case main_region_digitalwatch_Ticker_Editing_blinker_showSelection:
			return stateVector[3] == State.main_region_digitalwatch_Ticker_Editing_blinker_showSelection;
		case main_region_digitalwatch_Ticker_Editing_idleController_idle:
			return stateVector[4] == State.main_region_digitalwatch_Ticker_Editing_idleController_idle;
		case main_region_digitalwatch_Ticker_Editing_idleController_idleReset:
			return stateVector[4] == State.main_region_digitalwatch_Ticker_Editing_idleController_idleReset;
		case main_region_digitalwatch_Light_LightOff:
			return stateVector[6] == State.main_region_digitalwatch_Light_LightOff;
		case main_region_digitalwatch_Light_LightOn:
			return stateVector[6] == State.main_region_digitalwatch_Light_LightOn;
		case main_region_digitalwatch_Light_Wait:
			return stateVector[6] == State.main_region_digitalwatch_Light_Wait;
		case main_region_digitalwatch_Alarm_idle:
			return stateVector[7] == State.main_region_digitalwatch_Alarm_idle;
		case main_region_digitalwatch_Alarm_AlarmOn:
			return stateVector[7] == State.main_region_digitalwatch_Alarm_AlarmOn;
		case main_region_digitalwatch_Alarm_ActivateAlarm:
			return stateVector[7] == State.main_region_digitalwatch_Alarm_ActivateAlarm;
		case main_region_digitalwatch_Alarm_AlarmOff:
			return stateVector[7] == State.main_region_digitalwatch_Alarm_AlarmOff;
		case main_region_digitalwatch_AlarmFlasher_idle:
			return stateVector[8] == State.main_region_digitalwatch_AlarmFlasher_idle;
		case main_region_digitalwatch_AlarmFlasher_AlarmFlashOn:
			return stateVector[8] == State.main_region_digitalwatch_AlarmFlasher_AlarmFlashOn;
		case main_region_digitalwatch_AlarmFlasher_AlarmFlashOff:
			return stateVector[8] == State.main_region_digitalwatch_AlarmFlasher_AlarmFlashOff;
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
	
	private boolean check_main_region_digitalwatch_ChronoTicker_Tick_tr0_tr0() {
		return timeEvents[0];
	}
	
	private boolean check_main_region_digitalwatch_ChronoTicker_Tick_tr1_tr1() {
		return (sCIButtons.bottomRightPressed) && (isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay));
	}
	
	private boolean check_main_region_digitalwatch_ChronoTicker_Stopped_tr0_tr0() {
		return (sCIButtons.bottomRightPressed) && (isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay));
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
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr2_tr2() {
		return sCIButtons.bottomLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr3_tr3() {
		return sCIButtons.bottomRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr0_tr0() {
		return sCIInternal.chronoUpdate;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr1_tr1() {
		return sCIButtons.topLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr2_tr2() {
		return sCIButtons.bottomLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_Resetting_tr0_tr0() {
		return timeEvents[2];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr0_tr0() {
		return sCIButtons.bottomLeftReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr1_tr1() {
		return timeEvents[3];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_tr0_tr0() {
		return sCIButtons.bottomLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr0_tr0() {
		return sCIButtons.bottomLeftReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr1_tr1() {
		return timeEvents[4];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_tr0_tr0() {
		return sCIButtons.bottomRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr0_tr0() {
		return sCIButtons.bottomRightReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr1_tr1() {
		return timeEvents[5];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_tr0_tr0() {
		return timeEvents[6];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr0_tr0() {
		return timeEvents[7];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr1_tr1() {
		return isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection);
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking_tr0_tr0() {
		return sCIButtons.bottomLeftReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr0_tr0() {
		return sCIButtons.bottomLeftPressed || sCIButtons.bottomRightPressed || sCIButtons.topRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr1_tr1() {
		return timeEvents[8];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr0_tr0() {
		return (timeEvents[9]) && ( !isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection));
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr1_tr1() {
		return sCIButtons.bottomLeftReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr0_tr0() {
		return sCIButtons.bottomRightReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr1_tr1() {
		return timeEvents[10];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_increase_idle_tr0_tr0() {
		return sCIButtons.bottomLeftPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr0_tr0() {
		return sCIButtons.bottomLeftReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr1_tr1() {
		return timeEvents[11];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_tr0_tr0() {
		return sCIButtons.bottomRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr0_tr0() {
		return sCIButtons.bottomRightReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr1_tr1() {
		return timeEvents[12];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_tr0_tr0() {
		return (timeEvents[13]) && (isStateActive(State.main_region_digitalwatch_Ticker_Editing_increase_idle));
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_blinker_showSelection_tr0_tr0() {
		return timeEvents[14];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr0_tr0() {
		return timeEvents[15];
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr1_tr1() {
		return sCIButtons.bottomLeftPressed || sCIButtons.bottomRightPressed || sCIButtons.topRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr0_tr0() {
		return sCIButtons.bottomLeftReleased;
	}
	
	private boolean check_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr1_tr1() {
		return (timeEvents[16]) && ( !isStateActive(State.main_region_digitalwatch_Ticker_Editing_increase_increaseSelection));
	}
	
	private boolean check_main_region_digitalwatch_Light_LightOff_tr0_tr0() {
		return sCIButtons.topRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Light_LightOn_tr0_tr0() {
		return sCIButtons.topRightReleased;
	}
	
	private boolean check_main_region_digitalwatch_Light_Wait_tr0_tr0() {
		return timeEvents[17];
	}
	
	private boolean check_main_region_digitalwatch_Alarm_idle_tr0_tr0() {
		return isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit);
	}
	
	private boolean check_main_region_digitalwatch_Alarm_AlarmOn_tr0_tr0() {
		return (sCIButtons.bottomLeftPressed) && (isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit));
	}
	
	private boolean check_main_region_digitalwatch_Alarm_AlarmOn_tr1_tr1() {
		return sCILogicUnit.startAlarm;
	}
	
	private boolean check_main_region_digitalwatch_Alarm_ActivateAlarm_tr0_tr0() {
		return timeEvents[18] || sCIButtons.bottomLeftPressed || sCIButtons.bottomRightPressed || sCIButtons.topLeftPressed || sCIButtons.topRightPressed;
	}
	
	private boolean check_main_region_digitalwatch_Alarm_AlarmOff_tr0_tr0() {
		return (sCIButtons.bottomLeftPressed) && (isStateActive(State.main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit));
	}
	
	private boolean check_main_region_digitalwatch_AlarmFlasher_idle_tr0_tr0() {
		return isStateActive(State.main_region_digitalwatch_Alarm_ActivateAlarm);
	}
	
	private boolean check_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr0_tr0() {
		return timeEvents[19];
	}
	
	private boolean check_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr1_tr1() {
		return !isStateActive(State.main_region_digitalwatch_Alarm_ActivateAlarm);
	}
	
	private boolean check_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff_tr0_tr0() {
		return timeEvents[20];
	}
	
	private void effect_main_region_digitalwatch_ChronoTicker_Tick_tr0() {
		exitSequence_main_region_digitalwatch_ChronoTicker_Tick();
		sCIInternal.raiseChronoUpdate();
		
		enterSequence_main_region_digitalwatch_ChronoTicker_Tick_default();
	}
	
	private void effect_main_region_digitalwatch_ChronoTicker_Tick_tr1() {
		exitSequence_main_region_digitalwatch_ChronoTicker_Tick();
		enterSequence_main_region_digitalwatch_ChronoTicker_Stopped_default();
	}
	
	private void effect_main_region_digitalwatch_ChronoTicker_Stopped_tr0() {
		exitSequence_main_region_digitalwatch_ChronoTicker_Stopped();
		enterSequence_main_region_digitalwatch_ChronoTicker_Tick_default();
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
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr2() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr3() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr2() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_Resetting_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting();
		sCIInternal.raiseChronoUpdate();
		
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
		react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection__exit_Default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
		react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller__exit_Default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Main();
		enterSequence_main_region_digitalwatch_Ticker_Editing_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing();
		enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_increase_idle_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_increase_idle();
		enterSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
		enterSequence_main_region_digitalwatch_Ticker_Editing_increase_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
		enterSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle();
		enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
		enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
		react_main_region_digitalwatch_Ticker_Editing_changeSelection__exit_Default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
		enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_blinker_showSelection_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
		enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle();
		react_main_region_digitalwatch_Ticker_Editing_idleController__exit_Default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle();
		enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr0() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
		enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle_default();
	}
	
	private void effect_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr1() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
		enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle_default();
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
	
	private void effect_main_region_digitalwatch_Alarm_idle_tr0() {
		exitSequence_main_region_digitalwatch_Alarm_idle();
		enterSequence_main_region_digitalwatch_Alarm_AlarmOn_default();
	}
	
	private void effect_main_region_digitalwatch_Alarm_AlarmOn_tr0() {
		exitSequence_main_region_digitalwatch_Alarm_AlarmOn();
		enterSequence_main_region_digitalwatch_Alarm_AlarmOff_default();
	}
	
	private void effect_main_region_digitalwatch_Alarm_AlarmOn_tr1() {
		exitSequence_main_region_digitalwatch_Alarm_AlarmOn();
		enterSequence_main_region_digitalwatch_Alarm_ActivateAlarm_default();
	}
	
	private void effect_main_region_digitalwatch_Alarm_ActivateAlarm_tr0() {
		exitSequence_main_region_digitalwatch_Alarm_ActivateAlarm();
		enterSequence_main_region_digitalwatch_Alarm_AlarmOn_default();
	}
	
	private void effect_main_region_digitalwatch_Alarm_AlarmOff_tr0() {
		exitSequence_main_region_digitalwatch_Alarm_AlarmOff();
		enterSequence_main_region_digitalwatch_Alarm_AlarmOn_default();
	}
	
	private void effect_main_region_digitalwatch_AlarmFlasher_idle_tr0() {
		exitSequence_main_region_digitalwatch_AlarmFlasher_idle();
		enterSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_default();
	}
	
	private void effect_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr0() {
		exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
		enterSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff_default();
	}
	
	private void effect_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr1() {
		exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
		enterSequence_main_region_digitalwatch_AlarmFlasher_idle_default();
	}
	
	private void effect_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff_tr0() {
		exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff();
		enterSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_default();
	}
	
	/* Entry action for state 'Tick'. */
	private void entryAction_main_region_digitalwatch_ChronoTicker_Tick() {
		timer.setTimer(this, 0, 10, false);
		
		sCILogicUnit.operationCallback.increaseChronoByOne();
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
	
	/* Entry action for state 'Resetting'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_Resetting() {
		timer.setTimer(this, 2, 1, false);
		
		sCILogicUnit.operationCallback.resetChrono();
	}
	
	/* Entry action for state 'toAlarmEdit'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit() {
		timer.setTimer(this, 3, 1500, false);
	}
	
	/* Entry action for state 'AlarmEditing'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing() {
		sCILogicUnit.operationCallback.startAlarmEditMode();
	}
	
	/* Entry action for state 'increaseSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection() {
		timer.setTimer(this, 4, 300, false);
		
		sCILogicUnit.operationCallback.increaseSelection();
		
		sCIDisplay.operationCallback.refreshAlarmDisplay();
		
		sCIDisplay.operationCallback.showSelection();
	}
	
	/* Entry action for state 'increaseSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection() {
		timer.setTimer(this, 5, 2*1000, false);
		
		sCILogicUnit.operationCallback.selectNext();
		
		sCIDisplay.operationCallback.refreshAlarmDisplay();
	}
	
	/* Entry action for state 'hideSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection() {
		timer.setTimer(this, 6, 250, false);
		
		sCIDisplay.operationCallback.hideSelection();
	}
	
	/* Entry action for state 'showSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection() {
		timer.setTimer(this, 7, 250, false);
		
		sCIDisplay.operationCallback.showSelection();
	}
	
	/* Entry action for state 'idle'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle() {
		timer.setTimer(this, 8, 5*1000, false);
	}
	
	/* Entry action for state 'idleReset'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset() {
		timer.setTimer(this, 9, 1, false);
	}
	
	/* Entry action for state 'toTimeEdit'. */
	private void entryAction_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit() {
		timer.setTimer(this, 10, 1500, false);
	}
	
	/* Entry action for state 'Editing'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing() {
		sCILogicUnit.operationCallback.startTimeEditMode();
	}
	
	/* Entry action for state 'increaseSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection() {
		timer.setTimer(this, 11, 300, false);
		
		sCILogicUnit.operationCallback.increaseSelection();
		
		sCIDisplay.operationCallback.refreshTimeDisplay();
		
		sCIDisplay.operationCallback.refreshDateDisplay();
	}
	
	/* Entry action for state 'increaseSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection() {
		timer.setTimer(this, 12, 2*1000, false);
		
		sCILogicUnit.operationCallback.selectNext();
		
		sCIDisplay.operationCallback.refreshAlarmDisplay();
	}
	
	/* Entry action for state 'hideSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection() {
		timer.setTimer(this, 13, 250, false);
		
		sCIDisplay.operationCallback.hideSelection();
	}
	
	/* Entry action for state 'showSelection'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing_blinker_showSelection() {
		timer.setTimer(this, 14, 250, false);
		
		sCIDisplay.operationCallback.showSelection();
	}
	
	/* Entry action for state 'idle'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing_idleController_idle() {
		timer.setTimer(this, 15, 5*1000, false);
	}
	
	/* Entry action for state 'idleReset'. */
	private void entryAction_main_region_digitalwatch_Ticker_Editing_idleController_idleReset() {
		timer.setTimer(this, 16, 1, false);
	}
	
	/* Entry action for state 'LightOn'. */
	private void entryAction_main_region_digitalwatch_Light_LightOn() {
		sCIDisplay.operationCallback.setIndiglo();
	}
	
	/* Entry action for state 'Wait'. */
	private void entryAction_main_region_digitalwatch_Light_Wait() {
		timer.setTimer(this, 17, 2*1000, false);
	}
	
	/* Entry action for state 'AlarmOn'. */
	private void entryAction_main_region_digitalwatch_Alarm_AlarmOn() {
		sCILogicUnit.operationCallback.setAlarm();
		
		sCIDisplay.operationCallback.refreshAlarmDisplay();
	}
	
	/* Entry action for state 'ActivateAlarm'. */
	private void entryAction_main_region_digitalwatch_Alarm_ActivateAlarm() {
		timer.setTimer(this, 18, 4*1000, false);
	}
	
	/* Entry action for state 'AlarmOff'. */
	private void entryAction_main_region_digitalwatch_Alarm_AlarmOff() {
		sCILogicUnit.operationCallback.setAlarm();
		
		sCIDisplay.operationCallback.refreshAlarmDisplay();
	}
	
	/* Entry action for state 'idle'. */
	private void entryAction_main_region_digitalwatch_AlarmFlasher_idle() {
		sCIDisplay.operationCallback.unsetIndiglo();
	}
	
	/* Entry action for state 'AlarmFlashOn'. */
	private void entryAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn() {
		timer.setTimer(this, 19, 250, false);
		
		sCIDisplay.operationCallback.setIndiglo();
	}
	
	/* Entry action for state 'AlarmFlashOff'. */
	private void entryAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff() {
		timer.setTimer(this, 20, 250, false);
		
		sCIDisplay.operationCallback.unsetIndiglo();
	}
	
	/* Exit action for state 'Tick'. */
	private void exitAction_main_region_digitalwatch_ChronoTicker_Tick() {
		timer.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'Tick'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Ticker_Tick() {
		timer.unsetTimer(this, 1);
	}
	
	/* Exit action for state 'Resetting'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_Resetting() {
		timer.unsetTimer(this, 2);
	}
	
	/* Exit action for state 'toAlarmEdit'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit() {
		timer.unsetTimer(this, 3);
	}
	
	/* Exit action for state 'increaseSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection() {
		timer.unsetTimer(this, 4);
	}
	
	/* Exit action for state 'increaseSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection() {
		timer.unsetTimer(this, 5);
	}
	
	/* Exit action for state 'hideSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection() {
		timer.unsetTimer(this, 6);
	}
	
	/* Exit action for state 'showSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection() {
		timer.unsetTimer(this, 7);
	}
	
	/* Exit action for state 'idle'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle() {
		timer.unsetTimer(this, 8);
	}
	
	/* Exit action for state 'idleReset'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset() {
		timer.unsetTimer(this, 9);
	}
	
	/* Exit action for state 'toTimeEdit'. */
	private void exitAction_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit() {
		timer.unsetTimer(this, 10);
	}
	
	/* Exit action for state 'increaseSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection() {
		timer.unsetTimer(this, 11);
	}
	
	/* Exit action for state 'increaseSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection() {
		timer.unsetTimer(this, 12);
	}
	
	/* Exit action for state 'hideSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection() {
		timer.unsetTimer(this, 13);
	}
	
	/* Exit action for state 'showSelection'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing_blinker_showSelection() {
		timer.unsetTimer(this, 14);
	}
	
	/* Exit action for state 'idle'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing_idleController_idle() {
		timer.unsetTimer(this, 15);
	}
	
	/* Exit action for state 'idleReset'. */
	private void exitAction_main_region_digitalwatch_Ticker_Editing_idleController_idleReset() {
		timer.unsetTimer(this, 16);
	}
	
	/* Exit action for state 'Wait'. */
	private void exitAction_main_region_digitalwatch_Light_Wait() {
		timer.unsetTimer(this, 17);
		
		sCIDisplay.operationCallback.unsetIndiglo();
	}
	
	/* Exit action for state 'ActivateAlarm'. */
	private void exitAction_main_region_digitalwatch_Alarm_ActivateAlarm() {
		timer.unsetTimer(this, 18);
		
		sCILogicUnit.operationCallback.setAlarm();
		
		sCIDisplay.operationCallback.refreshAlarmDisplay();
	}
	
	/* Exit action for state 'AlarmFlashOn'. */
	private void exitAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn() {
		timer.unsetTimer(this, 19);
	}
	
	/* Exit action for state 'AlarmFlashOff'. */
	private void exitAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff() {
		timer.unsetTimer(this, 20);
	}
	
	/* 'default' enter sequence for state digitalwatch */
	private void enterSequence_main_region_digitalwatch_default() {
		enterSequence_main_region_digitalwatch_ChronoTicker_default();
		enterSequence_main_region_digitalwatch_Ticker_default();
		enterSequence_main_region_digitalwatch_Light_default();
		enterSequence_main_region_digitalwatch_Alarm_default();
		enterSequence_main_region_digitalwatch_AlarmFlasher_default();
	}
	
	/* 'default' enter sequence for state Tick */
	private void enterSequence_main_region_digitalwatch_ChronoTicker_Tick_default() {
		entryAction_main_region_digitalwatch_ChronoTicker_Tick();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_digitalwatch_ChronoTicker_Tick;
	}
	
	/* 'default' enter sequence for state Stopped */
	private void enterSequence_main_region_digitalwatch_ChronoTicker_Stopped_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_digitalwatch_ChronoTicker_Stopped;
	}
	
	/* 'default' enter sequence for state Main */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_default();
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
	
	/* 'default' enter sequence for state Resetting */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_Resetting();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_Resetting;
	}
	
	/* 'default' enter sequence for state toAlarmEdit */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit;
	}
	
	/* 'default' enter sequence for state AlarmEditing */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_default();
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_default();
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_default() {
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle;
	}
	
	/* 'default' enter sequence for state increaseSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection;
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_default() {
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle;
	}
	
	/* 'default' enter sequence for state increaseSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection;
	}
	
	/* 'default' enter sequence for state hideSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
		nextStateIndex = 4;
		stateVector[4] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection;
	}
	
	/* 'default' enter sequence for state showSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
		nextStateIndex = 4;
		stateVector[4] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection;
	}
	
	/* 'default' enter sequence for state stopBlinking */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking_default() {
		nextStateIndex = 4;
		stateVector[4] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking;
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
		nextStateIndex = 5;
		stateVector[5] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle;
	}
	
	/* 'default' enter sequence for state idleReset */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
		nextStateIndex = 5;
		stateVector[5] = State.main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset;
	}
	
	/* 'default' enter sequence for state toTimeEdit */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_default() {
		entryAction_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Main_Update_toTimeEdit;
	}
	
	/* 'default' enter sequence for state Editing */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing();
		enterSequence_main_region_digitalwatch_Ticker_Editing_increase_default();
		enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_default();
		enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_default();
		enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_default();
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_increase_idle_default() {
		nextStateIndex = 1;
		stateVector[1] = State.main_region_digitalwatch_Ticker_Editing_increase_idle;
	}
	
	/* 'default' enter sequence for state increaseSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
		nextStateIndex = 1;
		stateVector[1] = State.main_region_digitalwatch_Ticker_Editing_increase_increaseSelection;
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_default() {
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Editing_changeSelection_idle;
	}
	
	/* 'default' enter sequence for state increaseSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
		nextStateIndex = 2;
		stateVector[2] = State.main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection;
	}
	
	/* 'default' enter sequence for state hideSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Ticker_Editing_blinker_hideSelection;
	}
	
	/* 'default' enter sequence for state showSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
		nextStateIndex = 3;
		stateVector[3] = State.main_region_digitalwatch_Ticker_Editing_blinker_showSelection;
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing_idleController_idle();
		nextStateIndex = 4;
		stateVector[4] = State.main_region_digitalwatch_Ticker_Editing_idleController_idle;
	}
	
	/* 'default' enter sequence for state idleReset */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_default() {
		entryAction_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
		nextStateIndex = 4;
		stateVector[4] = State.main_region_digitalwatch_Ticker_Editing_idleController_idleReset;
	}
	
	/* 'default' enter sequence for state LightOff */
	private void enterSequence_main_region_digitalwatch_Light_LightOff_default() {
		nextStateIndex = 6;
		stateVector[6] = State.main_region_digitalwatch_Light_LightOff;
	}
	
	/* 'default' enter sequence for state LightOn */
	private void enterSequence_main_region_digitalwatch_Light_LightOn_default() {
		entryAction_main_region_digitalwatch_Light_LightOn();
		nextStateIndex = 6;
		stateVector[6] = State.main_region_digitalwatch_Light_LightOn;
	}
	
	/* 'default' enter sequence for state Wait */
	private void enterSequence_main_region_digitalwatch_Light_Wait_default() {
		entryAction_main_region_digitalwatch_Light_Wait();
		nextStateIndex = 6;
		stateVector[6] = State.main_region_digitalwatch_Light_Wait;
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_Alarm_idle_default() {
		nextStateIndex = 7;
		stateVector[7] = State.main_region_digitalwatch_Alarm_idle;
	}
	
	/* 'default' enter sequence for state AlarmOn */
	private void enterSequence_main_region_digitalwatch_Alarm_AlarmOn_default() {
		entryAction_main_region_digitalwatch_Alarm_AlarmOn();
		nextStateIndex = 7;
		stateVector[7] = State.main_region_digitalwatch_Alarm_AlarmOn;
	}
	
	/* 'default' enter sequence for state ActivateAlarm */
	private void enterSequence_main_region_digitalwatch_Alarm_ActivateAlarm_default() {
		entryAction_main_region_digitalwatch_Alarm_ActivateAlarm();
		nextStateIndex = 7;
		stateVector[7] = State.main_region_digitalwatch_Alarm_ActivateAlarm;
	}
	
	/* 'default' enter sequence for state AlarmOff */
	private void enterSequence_main_region_digitalwatch_Alarm_AlarmOff_default() {
		entryAction_main_region_digitalwatch_Alarm_AlarmOff();
		nextStateIndex = 7;
		stateVector[7] = State.main_region_digitalwatch_Alarm_AlarmOff;
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_main_region_digitalwatch_AlarmFlasher_idle_default() {
		entryAction_main_region_digitalwatch_AlarmFlasher_idle();
		nextStateIndex = 8;
		stateVector[8] = State.main_region_digitalwatch_AlarmFlasher_idle;
	}
	
	/* 'default' enter sequence for state AlarmFlashOn */
	private void enterSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_default() {
		entryAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
		nextStateIndex = 8;
		stateVector[8] = State.main_region_digitalwatch_AlarmFlasher_AlarmFlashOn;
	}
	
	/* 'default' enter sequence for state AlarmFlashOff */
	private void enterSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff_default() {
		entryAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff();
		nextStateIndex = 8;
		stateVector[8] = State.main_region_digitalwatch_AlarmFlasher_AlarmFlashOff;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* 'default' enter sequence for region ChronoTicker */
	private void enterSequence_main_region_digitalwatch_ChronoTicker_default() {
		react_main_region_digitalwatch_ChronoTicker__entry_Default();
	}
	
	/* 'default' enter sequence for region Ticker */
	private void enterSequence_main_region_digitalwatch_Ticker_default() {
		react_main_region_digitalwatch_Ticker__entry_Default();
	}
	
	/* 'default' enter sequence for region Ticker */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Ticker_default() {
		react_main_region_digitalwatch_Ticker_Main_Ticker__entry_Default();
	}
	
	/* 'default' enter sequence for region Update */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_default() {
		react_main_region_digitalwatch_Ticker_Main_Update__entry_Default();
	}
	
	/* 'default' enter sequence for region increase */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_default() {
		react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase__entry_Default();
	}
	
	/* 'default' enter sequence for region changeSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_default() {
		react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection__entry_Default();
	}
	
	/* 'default' enter sequence for region blinker */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_default() {
		react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker__entry_Default();
	}
	
	/* 'default' enter sequence for region idleConstroller */
	private void enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_default() {
		react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller__entry_Default();
	}
	
	/* 'default' enter sequence for region increase */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_increase_default() {
		react_main_region_digitalwatch_Ticker_Editing_increase__entry_Default();
	}
	
	/* 'default' enter sequence for region changeSelection */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_default() {
		react_main_region_digitalwatch_Ticker_Editing_changeSelection__entry_Default();
	}
	
	/* 'default' enter sequence for region blinker */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_default() {
		react_main_region_digitalwatch_Ticker_Editing_blinker__entry_Default();
	}
	
	/* 'default' enter sequence for region idleController */
	private void enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_default() {
		react_main_region_digitalwatch_Ticker_Editing_idleController__entry_Default();
	}
	
	/* 'default' enter sequence for region Light */
	private void enterSequence_main_region_digitalwatch_Light_default() {
		react_main_region_digitalwatch_Light__entry_Default();
	}
	
	/* 'default' enter sequence for region Alarm */
	private void enterSequence_main_region_digitalwatch_Alarm_default() {
		react_main_region_digitalwatch_Alarm__entry_Default();
	}
	
	/* 'default' enter sequence for region AlarmFlasher */
	private void enterSequence_main_region_digitalwatch_AlarmFlasher_default() {
		react_main_region_digitalwatch_AlarmFlasher__entry_Default();
	}
	
	/* Default exit sequence for state Tick */
	private void exitSequence_main_region_digitalwatch_ChronoTicker_Tick() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_ChronoTicker_Tick();
	}
	
	/* Default exit sequence for state Stopped */
	private void exitSequence_main_region_digitalwatch_ChronoTicker_Stopped() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Main */
	private void exitSequence_main_region_digitalwatch_Ticker_Main() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Ticker();
		exitSequence_main_region_digitalwatch_Ticker_Main_Update();
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
	
	/* Default exit sequence for state Resetting */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_Resetting();
	}
	
	/* Default exit sequence for state toAlarmEdit */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
	}
	
	/* Default exit sequence for state AlarmEditing */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing() {
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase();
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection();
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker();
		exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
	}
	
	/* Default exit sequence for state increaseSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
	}
	
	/* Default exit sequence for state increaseSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
	}
	
	/* Default exit sequence for state hideSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection() {
		nextStateIndex = 4;
		stateVector[4] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
	}
	
	/* Default exit sequence for state showSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection() {
		nextStateIndex = 4;
		stateVector[4] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
	}
	
	/* Default exit sequence for state stopBlinking */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking() {
		nextStateIndex = 4;
		stateVector[4] = State.$NullState$;
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle() {
		nextStateIndex = 5;
		stateVector[5] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
	}
	
	/* Default exit sequence for state idleReset */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset() {
		nextStateIndex = 5;
		stateVector[5] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
	}
	
	/* Default exit sequence for state toTimeEdit */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
	}
	
	/* Default exit sequence for state Editing */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing() {
		exitSequence_main_region_digitalwatch_Ticker_Editing_increase();
		exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection();
		exitSequence_main_region_digitalwatch_Ticker_Editing_blinker();
		exitSequence_main_region_digitalwatch_Ticker_Editing_idleController();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_increase_idle() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}
	
	/* Default exit sequence for state increaseSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
	}
	
	/* Default exit sequence for state increaseSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
	}
	
	/* Default exit sequence for state hideSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
	}
	
	/* Default exit sequence for state showSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection() {
		nextStateIndex = 3;
		stateVector[3] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle() {
		nextStateIndex = 4;
		stateVector[4] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Editing_idleController_idle();
	}
	
	/* Default exit sequence for state idleReset */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset() {
		nextStateIndex = 4;
		stateVector[4] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
	}
	
	/* Default exit sequence for state LightOff */
	private void exitSequence_main_region_digitalwatch_Light_LightOff() {
		nextStateIndex = 6;
		stateVector[6] = State.$NullState$;
	}
	
	/* Default exit sequence for state LightOn */
	private void exitSequence_main_region_digitalwatch_Light_LightOn() {
		nextStateIndex = 6;
		stateVector[6] = State.$NullState$;
	}
	
	/* Default exit sequence for state Wait */
	private void exitSequence_main_region_digitalwatch_Light_Wait() {
		nextStateIndex = 6;
		stateVector[6] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Light_Wait();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_Alarm_idle() {
		nextStateIndex = 7;
		stateVector[7] = State.$NullState$;
	}
	
	/* Default exit sequence for state AlarmOn */
	private void exitSequence_main_region_digitalwatch_Alarm_AlarmOn() {
		nextStateIndex = 7;
		stateVector[7] = State.$NullState$;
	}
	
	/* Default exit sequence for state ActivateAlarm */
	private void exitSequence_main_region_digitalwatch_Alarm_ActivateAlarm() {
		nextStateIndex = 7;
		stateVector[7] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_Alarm_ActivateAlarm();
	}
	
	/* Default exit sequence for state AlarmOff */
	private void exitSequence_main_region_digitalwatch_Alarm_AlarmOff() {
		nextStateIndex = 7;
		stateVector[7] = State.$NullState$;
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_main_region_digitalwatch_AlarmFlasher_idle() {
		nextStateIndex = 8;
		stateVector[8] = State.$NullState$;
	}
	
	/* Default exit sequence for state AlarmFlashOn */
	private void exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn() {
		nextStateIndex = 8;
		stateVector[8] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
	}
	
	/* Default exit sequence for state AlarmFlashOff */
	private void exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff() {
		nextStateIndex = 8;
		stateVector[8] = State.$NullState$;
		
		exitAction_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff();
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_digitalwatch_ChronoTicker_Tick:
			exitSequence_main_region_digitalwatch_ChronoTicker_Tick();
			break;
		case main_region_digitalwatch_ChronoTicker_Stopped:
			exitSequence_main_region_digitalwatch_ChronoTicker_Stopped();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
			break;
		case main_region_digitalwatch_Ticker_Editing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
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
		case main_region_digitalwatch_Ticker_Main_Update_Resetting:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_toTimeEdit:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
			break;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
			break;
		default:
			break;
		}
		
		switch (stateVector[3]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
			break;
		case main_region_digitalwatch_Ticker_Editing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Editing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
			break;
		default:
			break;
		}
		
		switch (stateVector[4]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking();
			break;
		case main_region_digitalwatch_Ticker_Editing_idleController_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_idleController_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
			break;
		default:
			break;
		}
		
		switch (stateVector[5]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
			break;
		default:
			break;
		}
		
		switch (stateVector[6]) {
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
		
		switch (stateVector[7]) {
		case main_region_digitalwatch_Alarm_idle:
			exitSequence_main_region_digitalwatch_Alarm_idle();
			break;
		case main_region_digitalwatch_Alarm_AlarmOn:
			exitSequence_main_region_digitalwatch_Alarm_AlarmOn();
			break;
		case main_region_digitalwatch_Alarm_ActivateAlarm:
			exitSequence_main_region_digitalwatch_Alarm_ActivateAlarm();
			break;
		case main_region_digitalwatch_Alarm_AlarmOff:
			exitSequence_main_region_digitalwatch_Alarm_AlarmOff();
			break;
		default:
			break;
		}
		
		switch (stateVector[8]) {
		case main_region_digitalwatch_AlarmFlasher_idle:
			exitSequence_main_region_digitalwatch_AlarmFlasher_idle();
			break;
		case main_region_digitalwatch_AlarmFlasher_AlarmFlashOn:
			exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
			break;
		case main_region_digitalwatch_AlarmFlasher_AlarmFlashOff:
			exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region ChronoTicker */
	private void exitSequence_main_region_digitalwatch_ChronoTicker() {
		switch (stateVector[0]) {
		case main_region_digitalwatch_ChronoTicker_Tick:
			exitSequence_main_region_digitalwatch_ChronoTicker_Tick();
			break;
		case main_region_digitalwatch_ChronoTicker_Stopped:
			exitSequence_main_region_digitalwatch_ChronoTicker_Stopped();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Ticker */
	private void exitSequence_main_region_digitalwatch_Ticker() {
		switch (stateVector[1]) {
		case main_region_digitalwatch_Ticker_Main_Ticker_Tick:
			exitSequence_main_region_digitalwatch_Ticker_Main_Ticker_Tick();
			break;
		case main_region_digitalwatch_Ticker_Editing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
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
		case main_region_digitalwatch_Ticker_Main_Update_Resetting:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_toTimeEdit:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
			break;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
			break;
		default:
			break;
		}
		
		switch (stateVector[3]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
			break;
		case main_region_digitalwatch_Ticker_Editing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Editing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
			break;
		default:
			break;
		}
		
		switch (stateVector[4]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking();
			break;
		case main_region_digitalwatch_Ticker_Editing_idleController_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_idleController_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
			break;
		default:
			break;
		}
		
		switch (stateVector[5]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
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
		case main_region_digitalwatch_Ticker_Main_Update_Resetting:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_Resetting();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_toTimeEdit:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
			break;
		default:
			break;
		}
		
		switch (stateVector[3]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
			break;
		default:
			break;
		}
		
		switch (stateVector[4]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking();
			break;
		default:
			break;
		}
		
		switch (stateVector[5]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region increase */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase() {
		switch (stateVector[2]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region changeSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection() {
		switch (stateVector[3]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region blinker */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker() {
		switch (stateVector[4]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region idleConstroller */
	private void exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller() {
		switch (stateVector[5]) {
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
			break;
		case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region increase */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_increase() {
		switch (stateVector[1]) {
		case main_region_digitalwatch_Ticker_Editing_increase_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_increase_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_increase_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region changeSelection */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection() {
		switch (stateVector[2]) {
		case main_region_digitalwatch_Ticker_Editing_changeSelection_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region blinker */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_blinker() {
		switch (stateVector[3]) {
		case main_region_digitalwatch_Ticker_Editing_blinker_hideSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
			break;
		case main_region_digitalwatch_Ticker_Editing_blinker_showSelection:
			exitSequence_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region idleController */
	private void exitSequence_main_region_digitalwatch_Ticker_Editing_idleController() {
		switch (stateVector[4]) {
		case main_region_digitalwatch_Ticker_Editing_idleController_idle:
			exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle();
			break;
		case main_region_digitalwatch_Ticker_Editing_idleController_idleReset:
			exitSequence_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Light */
	private void exitSequence_main_region_digitalwatch_Light() {
		switch (stateVector[6]) {
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
	
	/* Default exit sequence for region Alarm */
	private void exitSequence_main_region_digitalwatch_Alarm() {
		switch (stateVector[7]) {
		case main_region_digitalwatch_Alarm_idle:
			exitSequence_main_region_digitalwatch_Alarm_idle();
			break;
		case main_region_digitalwatch_Alarm_AlarmOn:
			exitSequence_main_region_digitalwatch_Alarm_AlarmOn();
			break;
		case main_region_digitalwatch_Alarm_ActivateAlarm:
			exitSequence_main_region_digitalwatch_Alarm_ActivateAlarm();
			break;
		case main_region_digitalwatch_Alarm_AlarmOff:
			exitSequence_main_region_digitalwatch_Alarm_AlarmOff();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region AlarmFlasher */
	private void exitSequence_main_region_digitalwatch_AlarmFlasher() {
		switch (stateVector[8]) {
		case main_region_digitalwatch_AlarmFlasher_idle:
			exitSequence_main_region_digitalwatch_AlarmFlasher_idle();
			break;
		case main_region_digitalwatch_AlarmFlasher_AlarmFlashOn:
			exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
			break;
		case main_region_digitalwatch_AlarmFlasher_AlarmFlashOff:
			exitSequence_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state Tick. */
	private void react_main_region_digitalwatch_ChronoTicker_Tick() {
		if (check_main_region_digitalwatch_ChronoTicker_Tick_tr0_tr0()) {
			effect_main_region_digitalwatch_ChronoTicker_Tick_tr0();
		} else {
			if (check_main_region_digitalwatch_ChronoTicker_Tick_tr1_tr1()) {
				effect_main_region_digitalwatch_ChronoTicker_Tick_tr1();
			}
		}
	}
	
	/* The reactions of state Stopped. */
	private void react_main_region_digitalwatch_ChronoTicker_Stopped() {
		if (check_main_region_digitalwatch_ChronoTicker_Stopped_tr0_tr0()) {
			effect_main_region_digitalwatch_ChronoTicker_Stopped_tr0();
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
			} else {
				if (check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr2_tr2()) {
					effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr2();
				} else {
					if (check_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr3_tr3()) {
						effect_main_region_digitalwatch_Ticker_Main_Update_TimeDisplay_tr3();
					}
				}
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
			} else {
				if (check_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr2_tr2()) {
					effect_main_region_digitalwatch_Ticker_Main_Update_ChronoDisplay_tr2();
				}
			}
		}
	}
	
	/* The reactions of state Resetting. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_Resetting() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_Resetting_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_Resetting_tr0();
		}
	}
	
	/* The reactions of state toAlarmEdit. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit_tr1();
			}
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_tr0();
		}
	}
	
	/* The reactions of state increaseSelection. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection_tr1();
			}
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_tr0();
		}
	}
	
	/* The reactions of state increaseSelection. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection_tr1();
			}
		}
	}
	
	/* The reactions of state hideSelection. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_tr0();
		}
	}
	
	/* The reactions of state showSelection. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection_tr1();
			}
		}
	}
	
	/* The reactions of state stopBlinking. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking_tr0();
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_tr1();
			}
		}
	}
	
	/* The reactions of state idleReset. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset_tr1();
			}
		}
	}
	
	/* The reactions of state toTimeEdit. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit() {
		if (check_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit_tr1();
			}
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Ticker_Editing_increase_idle() {
		if (check_main_region_digitalwatch_Ticker_Editing_increase_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_increase_idle_tr0();
		}
	}
	
	/* The reactions of state increaseSelection. */
	private void react_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection() {
		if (check_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection_tr1();
			}
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Ticker_Editing_changeSelection_idle() {
		if (check_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_tr0();
		}
	}
	
	/* The reactions of state increaseSelection. */
	private void react_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection() {
		if (check_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection_tr1();
			}
		}
	}
	
	/* The reactions of state hideSelection. */
	private void react_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection() {
		if (check_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_tr0();
		}
	}
	
	/* The reactions of state showSelection. */
	private void react_main_region_digitalwatch_Ticker_Editing_blinker_showSelection() {
		if (check_main_region_digitalwatch_Ticker_Editing_blinker_showSelection_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_blinker_showSelection_tr0();
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Ticker_Editing_idleController_idle() {
		if (check_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Editing_idleController_idle_tr1();
			}
		}
	}
	
	/* The reactions of state idleReset. */
	private void react_main_region_digitalwatch_Ticker_Editing_idleController_idleReset() {
		if (check_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr0_tr0()) {
			effect_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr0();
		} else {
			if (check_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr1_tr1()) {
				effect_main_region_digitalwatch_Ticker_Editing_idleController_idleReset_tr1();
			}
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
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_Alarm_idle() {
		if (check_main_region_digitalwatch_Alarm_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_Alarm_idle_tr0();
		}
	}
	
	/* The reactions of state AlarmOn. */
	private void react_main_region_digitalwatch_Alarm_AlarmOn() {
		if (check_main_region_digitalwatch_Alarm_AlarmOn_tr0_tr0()) {
			effect_main_region_digitalwatch_Alarm_AlarmOn_tr0();
		} else {
			if (check_main_region_digitalwatch_Alarm_AlarmOn_tr1_tr1()) {
				effect_main_region_digitalwatch_Alarm_AlarmOn_tr1();
			}
		}
	}
	
	/* The reactions of state ActivateAlarm. */
	private void react_main_region_digitalwatch_Alarm_ActivateAlarm() {
		if (check_main_region_digitalwatch_Alarm_ActivateAlarm_tr0_tr0()) {
			effect_main_region_digitalwatch_Alarm_ActivateAlarm_tr0();
		}
	}
	
	/* The reactions of state AlarmOff. */
	private void react_main_region_digitalwatch_Alarm_AlarmOff() {
		if (check_main_region_digitalwatch_Alarm_AlarmOff_tr0_tr0()) {
			effect_main_region_digitalwatch_Alarm_AlarmOff_tr0();
		}
	}
	
	/* The reactions of state idle. */
	private void react_main_region_digitalwatch_AlarmFlasher_idle() {
		if (check_main_region_digitalwatch_AlarmFlasher_idle_tr0_tr0()) {
			effect_main_region_digitalwatch_AlarmFlasher_idle_tr0();
		}
	}
	
	/* The reactions of state AlarmFlashOn. */
	private void react_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn() {
		if (check_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr0_tr0()) {
			effect_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr0();
		} else {
			if (check_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr1_tr1()) {
				effect_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn_tr1();
			}
		}
	}
	
	/* The reactions of state AlarmFlashOff. */
	private void react_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff() {
		if (check_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff_tr0_tr0()) {
			effect_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff_tr0();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_digitalwatch_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_ChronoTicker__entry_Default() {
		enterSequence_main_region_digitalwatch_ChronoTicker_Stopped_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_default();
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
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Editing_increase__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Editing_increase_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Editing_changeSelection__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Editing_changeSelection_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Editing_blinker__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Ticker_Editing_idleController__entry_Default() {
		enterSequence_main_region_digitalwatch_Ticker_Editing_idleController_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Light__entry_Default() {
		enterSequence_main_region_digitalwatch_Light_LightOff_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_Alarm__entry_Default() {
		enterSequence_main_region_digitalwatch_Alarm_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region_digitalwatch_AlarmFlasher__entry_Default() {
		enterSequence_main_region_digitalwatch_AlarmFlasher_idle_default();
	}
	
	/* The reactions of exit default. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection__exit_Default() {
		effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller__exit_Default() {
		effect_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_main_region_digitalwatch_Ticker_Editing_changeSelection__exit_Default() {
		effect_main_region_digitalwatch_Ticker_Editing_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_main_region_digitalwatch_Ticker_Editing_idleController__exit_Default() {
		effect_main_region_digitalwatch_Ticker_Editing_tr0();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_digitalwatch_ChronoTicker_Tick:
				react_main_region_digitalwatch_ChronoTicker_Tick();
				break;
			case main_region_digitalwatch_ChronoTicker_Stopped:
				react_main_region_digitalwatch_ChronoTicker_Stopped();
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
			case main_region_digitalwatch_Ticker_Main_Update_Resetting:
				react_main_region_digitalwatch_Ticker_Main_Update_Resetting();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit:
				react_main_region_digitalwatch_Ticker_Main_Update_toAlarmEdit();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_idle();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_increase_increaseSelection();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_idle();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_changeSelection_increaseSelection();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_hideSelection();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_showSelection();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_blinker_stopBlinking();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idle();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset:
				react_main_region_digitalwatch_Ticker_Main_Update_AlarmEditing_idleConstroller_idleReset();
				break;
			case main_region_digitalwatch_Ticker_Main_Update_toTimeEdit:
				react_main_region_digitalwatch_Ticker_Main_Update_toTimeEdit();
				break;
			case main_region_digitalwatch_Ticker_Editing_increase_idle:
				react_main_region_digitalwatch_Ticker_Editing_increase_idle();
				break;
			case main_region_digitalwatch_Ticker_Editing_increase_increaseSelection:
				react_main_region_digitalwatch_Ticker_Editing_increase_increaseSelection();
				break;
			case main_region_digitalwatch_Ticker_Editing_changeSelection_idle:
				react_main_region_digitalwatch_Ticker_Editing_changeSelection_idle();
				break;
			case main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection:
				react_main_region_digitalwatch_Ticker_Editing_changeSelection_increaseSelection();
				break;
			case main_region_digitalwatch_Ticker_Editing_blinker_hideSelection:
				react_main_region_digitalwatch_Ticker_Editing_blinker_hideSelection();
				break;
			case main_region_digitalwatch_Ticker_Editing_blinker_showSelection:
				react_main_region_digitalwatch_Ticker_Editing_blinker_showSelection();
				break;
			case main_region_digitalwatch_Ticker_Editing_idleController_idle:
				react_main_region_digitalwatch_Ticker_Editing_idleController_idle();
				break;
			case main_region_digitalwatch_Ticker_Editing_idleController_idleReset:
				react_main_region_digitalwatch_Ticker_Editing_idleController_idleReset();
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
			case main_region_digitalwatch_Alarm_idle:
				react_main_region_digitalwatch_Alarm_idle();
				break;
			case main_region_digitalwatch_Alarm_AlarmOn:
				react_main_region_digitalwatch_Alarm_AlarmOn();
				break;
			case main_region_digitalwatch_Alarm_ActivateAlarm:
				react_main_region_digitalwatch_Alarm_ActivateAlarm();
				break;
			case main_region_digitalwatch_Alarm_AlarmOff:
				react_main_region_digitalwatch_Alarm_AlarmOff();
				break;
			case main_region_digitalwatch_AlarmFlasher_idle:
				react_main_region_digitalwatch_AlarmFlasher_idle();
				break;
			case main_region_digitalwatch_AlarmFlasher_AlarmFlashOn:
				react_main_region_digitalwatch_AlarmFlasher_AlarmFlashOn();
				break;
			case main_region_digitalwatch_AlarmFlasher_AlarmFlashOff:
				react_main_region_digitalwatch_AlarmFlasher_AlarmFlashOff();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
