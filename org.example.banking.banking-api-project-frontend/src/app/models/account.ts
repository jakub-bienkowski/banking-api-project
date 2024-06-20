import {Transaction} from "./transaction";

export interface Account {
  id: string;
  customerId: string;
  balance: number;
  transactions: Transaction[];
}
