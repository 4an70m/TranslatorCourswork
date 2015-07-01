package coursework.translator.analyzer.syntax.descending;

import java.util.LinkedList;

import coursework.translator.analyzer.util.handler.OutputHandler;
import coursework.translator.analyzer.util.output.ConstantTable;
import coursework.translator.analyzer.util.output.IdentifierTable;
import coursework.translator.analyzer.util.output.outputtable.OutputTable;

public class ProcedureHandler {

	OutputHandler outputHandler;
	OutputTable outputTable;
	ConstantTable constantTable;
	IdentifierTable idTable;
	LinkedList<Integer> errorCode;
	LinkedList<Integer> errorLine;

	private int lexemeNumber;

	public ProcedureHandler(OutputHandler outputHandler) {
		this.outputHandler = outputHandler;
		this.outputTable = outputHandler.getOutputTable();
		this.constantTable = outputHandler.getConstantTable();
		this.idTable = outputHandler.getIdTable();
		lexemeNumber = 0;
		errorCode = new LinkedList<>();
		errorLine = new LinkedList<>();
	}

	LinkedList<Integer> getErrorCode() {
		return errorCode;
	}

	LinkedList<Integer> getErrorLine() {
		return errorLine;
	}

	public int pProgram() {
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 1) { // program
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 35) {// id
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 2) {// var
					if (pVarList() == 0) {
						if (outputTable.get(lexemeNumber++).getLexemeCode() == 3) {// begin
							if (pOperationList() == 0) {
								if (outputTable.get(lexemeNumber++)
										.getLexemeCode() == 4) {// end.
									return 0;
								}
								errorCode.push(116);
								errorLine.push(outputTable.get(--lexemeNumber)
										.getLineNumber());
								return 116;
							}
						}
						errorCode.push(115);
						errorLine.push(outputTable.get(--lexemeNumber)
								.getLineNumber());
						return 115;
					}
					errorCode.push(117);
					errorLine.push(outputTable.get(--lexemeNumber)
							.getLineNumber());
					return 117;
				}
				errorCode.push(114);
				errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
				return 114;
			}
			errorCode.push(113);
			errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
			return 113;
		}
		errorCode.push(100);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 100;
	}

	public int pVarList() {

		if (pIdList() == 0) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 15) {
				if (pType() == 0) {
					if (outputTable.get(lexemeNumber++).getLexemeCode() == 13) {
						if (pVarList() == 0)
							return 0;
					} else {
						lexemeNumber--;
						return 0;
					}
				}
			}
			errorCode.push(119);
			errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
			return 119;
		}
		errorCode.push(101);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 101;
	}

	public int pIdList() {
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 14) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 35) {
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 14) {
					lexemeNumber--;
					if (pIdList() == 0) {
						return 0;
					}
				} else {
					lexemeNumber--;
					if (pIdList() == 0) {
						return 0;
					} else {
						lexemeNumber--;
						return 0;
					}
				}
			}
			errorCode.push(102);
			errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
			return 102;
		} else {
			lexemeNumber--;
		}
		return 0;
	}

	public int pType() {

		if (outputTable.get(lexemeNumber++).getLexemeCode() == 7) {
			return 0;
		}
		errorCode.push(103);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 103;
	}

	public int pOperationList() {
		if (outputTable.get(lexemeNumber) != null) {
			if (pOperation() == 0) {
				if (outputTable.get(lexemeNumber) != null
						&& outputTable.get(lexemeNumber++).getLexemeCode() == 13) {
					if (pOperationList() == 0) {
						return 0;
					}
				}
				lexemeNumber--;
				return 0;
			}
		}
		errorCode.push(104);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 104;
	}

	public int pOperation() {

		if (outputTable.get(lexemeNumber++).getLexemeCode() == 5) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 33) {
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 14) {
					if (outputTable.get(lexemeNumber++).getLexemeCode() == 35) {
						if (outputTable.get(lexemeNumber++).getLexemeCode() == 34) {
							return 0;
						}
					}
				}
			}
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 6) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 33) {
				if (pIdList() == 0) {
					if (outputTable.get(lexemeNumber++).getLexemeCode() == 34) {
						return 0;
					}
				}
			}
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 8) {
			if (pLogExpr() == 0) {
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 9) {
					if (pOperation() == 0) {
						return 0;
					}
				}
				errorCode.push(118);
				errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
				return 118;
			}
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 10) {
			if (pLogExpr() == 0) {
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 12) {
					if (pOperationList() == 0) {
						if (outputTable.get(lexemeNumber++).getLexemeCode() == 11) {
							if (pOperation() == 0) {
								return 0;
							}
						}
					}
				}
			}
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 35) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 16) {
				if (pMathExp() == 0) {
					return 0;
				}
			}
		}
		errorCode.push(105);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 105;
	}

	public int pLogExpr() {

		if (pLogTerm() == 0) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 30) {
				if (pLogTerm() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			return 0;
		}
		errorCode.push(106);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 106;
	}

	public int pLogTerm() {

		if (pLogMult() == 0) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 31) {
				if (pLogMult() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			return 0;
		}
		errorCode.push(107);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 107;
	}

	public int pLogMult() {

		if (outputTable.get(lexemeNumber++).getLexemeCode() == 27) {
			if (pLogMult() == 0) {
				return 0;
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 37) {
				if (pLogExpr() == 0) {
					if (outputTable.get(lexemeNumber++).getLexemeCode() == 38) {
						return 0;
					}
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 25) {
				return 0;
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 36) {
				return 0;
			}
		}

		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 37) {
			if (pLogExpr() == 0) {
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 38) {
					return 0;
				}
			}
		}
		lexemeNumber--;
		if (pLogRatio() == 0) {
			return 0;
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 25) {
			return 0;
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 36) {
			return 0;
		}
		errorCode.push(108);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 108;
	}

	public int pLogRatio() {

		if (pMathExp() == 0) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 21) {
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 22) {
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 23) {
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 24) {
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 28) {
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 29) {
				if (pMathExp() == 0) {
					return 0;
				}
			}

		}
		errorCode.push(109);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 109;
	}

	public int pMathExp() {

		if (pMathTerm() == 0) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 17) {// +
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 18) {// -
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			return 0;

		}
		errorCode.push(110);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 110;
	}

	public int pMathTerm() {

		if (pMathMult() == 0) {
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 20) {// *
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			if (outputTable.get(lexemeNumber++).getLexemeCode() == 19) {// /
				if (pMathExp() == 0) {
					return 0;
				}
			}
			lexemeNumber--;
			return 0;
		}
		errorCode.push(111);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 111;
	}

	public int pMathMult() {

		if (outputTable.get(lexemeNumber++).getLexemeCode() == 35) {// id
			return 0;
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 36) {// const
			return 0;
		}
		lexemeNumber--;
		if (outputTable.get(lexemeNumber++).getLexemeCode() == 33) {// (
			if (pMathExp() == 0) {
				if (outputTable.get(lexemeNumber++).getLexemeCode() == 34) {// )
					return 0;
				}
			}
		}
		errorCode.push(112);
		errorLine.push(outputTable.get(--lexemeNumber).getLineNumber());
		return 112;
	}

}
