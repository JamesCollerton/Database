// This is an enumerated class corresponding to commands that can be parsed.

// Responsibilites: Holding and enumerating all of the possible actions you
// 					can take.

public enum Action
{
	NO_COMMAND,
	CREATE_DATABASE,
	CREATE_TABLE,
	EXIT_DB,
	SELECT_TABLE,
	ADD_RECORD,
	PRINT_TABLE,
	ALTER_COLS,
	GET_COLS,
	GET_NUM_COLS,
	GET_NUM_ROWS,
	GET_RECORD,
	LOAD_FROM_FILE,
	SELECT_DATABASE,
	DELETE_RECORD,
	WRITE_FILE,
	READ_FILE,
	LEAVE_TABLE,
	LEAVE_DATABASE,
	START_GUI,
	LIST_DATABASES,
	LIST_TABLES
}