// src/types.ts
export interface TabProps {
  active?: boolean;
}

export interface ProfileData {
  cnsno: number;
  name: string;
  gendercd: string;
  categcd: string;
  email: string;
  phonenum: string;
  birthymd: string;
  introduce: string;
  quitymd: string;
  imgname: string;
  cnscareer: string;
  cnsproject: string;
  credt: string;
  upddt: string;
  password: string;
  rolecd: string;
  cnscareer_vo: Career[]; 
}

export interface Education {
  cnsno: number;
  seqno: number;
  term: string;
  content: string;
  detail: string;
  careerdiv: string;
  credt: string;
  upddt: string;
}

export interface Career {
  cnsno: number;
  seqno: number;
  term: string;
  content: string;
  detail: string;
  careerdiv: string; 
  credt: string;
  upddt: string;
}
