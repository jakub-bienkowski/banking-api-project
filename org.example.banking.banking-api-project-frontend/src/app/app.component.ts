import {Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {BackendService} from "./services/backend.service";
import {Customer} from "./models/customer";
import {MatDialog} from "@angular/material/dialog";
import {AddAccountModalComponent} from "./components/add-account-modal/add-account-modal.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  private currentLang: string = 'en-GB';
  protected customers: Customer[] = [];
  protected customerDetails: Customer | undefined;
  protected accountDisplayColumns: string[] = ['id', 'balance', 'transactions'];

  constructor(private translateService: TranslateService, private backendService: BackendService, private dialog: MatDialog) {
    this.translateService.setDefaultLang(this.currentLang);
  }

  ngOnInit(): void {
    this.backendService.getAllCustomers().subscribe(customers => {
      this.customers = customers;
    });
  }

  loadCustomer(customerId: string) {
    this.backendService.getCustomer(customerId).subscribe(customer => {
      this.customerDetails = customer;
    });
  }

  onAddAccount(customerId: string) {
    const dialogRef = this.dialog.open(AddAccountModalComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.createAccount(customerId, result);
      }
    });
  }

  createAccount(customerId: string, initialCredit: number) {
    let addAccountRequest = {
      customerId: customerId,
      initialCredit: initialCredit
    };
    this.backendService.createAccount(addAccountRequest).subscribe(account => {
      this.loadCustomer(customerId);
    });
  }
}
