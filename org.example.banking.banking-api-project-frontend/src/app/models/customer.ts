import {Account} from "./account";

export interface Customer {
  id: string;
  name: string;
  surname: string;
  accounts: Account[];
}
