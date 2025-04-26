export const basePositions = ["UBL", "UBR", "UFR", "UFL", "LUB", "LFU", "LDF", "LBD", "FUL", "FRU", "FDR", "FLD", "RUF", "RBU", "RDB", "RFD", "BUR", "BLU", "BDL", "BRD", "DFL", "DRF", "DBR", "DLB"];
export const cornerPositions = ["UBL", "UBR", "UFR", "UFL", "LUB", "LFU", "LDF", "LBD", "FUL", "FRU", "FDR", "FLD", "RUF", "RBU", "RDB", "RFD", "BUR", "BLU", "BDL", "BRD", "DFL", "DRF", "DBR", "DLB"];
export const edgePositions = ["UB", "UR", "UF", "UL", "LU", "LF", "LD", "LB", "FU", "FR", "FD", "FL", "RU", "RB", "RD", "RF", "BU", "BL", "BD", "BR", "DF", "DR", "DB", "DL"];
export const wingPositions = ["UBl", "URb", "UFr", "ULf", "LUf", "LFd", "LDb", "LBu", "FUr", "FRd", "FDl", "FLu", "RUb", "RBd", "RDf", "RFu", "BUl", "BLd", "BDr", "BRu", "DFr", "DRb", "DBl", "DLf"];
export const xCenterPositions = ["Ubl", "Urb", "Ufr", "Ulf", "Lub", "Lfu", "Ldf", "Lbd", "Ful", "Fru", "Fdr", "Fld", "Ruf", "Rbu", "Rdb", "Rfd", "Bur", "Blu", "Bdl", "Brd", "Dfl", "Drf", "Dbr", "Dlb"];
export const tCenterPositions = ["Ub", "Ur", "Uf", "Ul", "Lu", "Lf", "Ld", "Lb", "Fu", "Fr", "Fd", "Fl", "Ru", "Rb", "Rd", "Rf", "Bu", "Bl", "Bd", "Br", "Df", "Dr", "Db", "Dl"];

const getDefaultLetter = (index) => String.fromCharCode(65 + index); // A=65 in ASCII

export const defaultBaseLetters = basePositions.reduce((acc, pos, index) => {
  acc[pos] = getDefaultLetter(index);
  return acc;
}, {});

export const defaultCornerLetters = cornerPositions.reduce((acc, pos, index) => {
  acc[pos] = getDefaultLetter(index);
  return acc;
}, {});

export const defaultEdgeLetters = edgePositions.reduce((acc, pos, index) => {
  acc[pos] = getDefaultLetter(index);
  return acc;
}, {});

export const defaultWingLetters = wingPositions.reduce((acc, pos, index) => {
  acc[pos] = getDefaultLetter(index);
  return acc;
}, {});

export const defaultXCenterLetters = xCenterPositions.reduce((acc, pos, index) => {
  acc[pos] = getDefaultLetter(index);
  return acc;
}, {});

export const defaultTCenterLetters = tCenterPositions.reduce((acc, pos, index) => {
  acc[pos] = getDefaultLetter(index);
  return acc;
}, {});

export const defaultLetterScheme = {
  corners: defaultCornerLetters,
  edges: defaultEdgeLetters,
  wings: defaultWingLetters,
  xCenters: defaultXCenterLetters,
  tCenters: defaultTCenterLetters
};