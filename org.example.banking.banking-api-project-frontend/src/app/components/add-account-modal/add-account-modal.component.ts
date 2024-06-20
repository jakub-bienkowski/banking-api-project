import { Component } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-account-modal',
  templateUrl: './add-account-modal.component.html',
  styleUrl: './add-account-modal.component.scss'
})
export class AddAccountModalComponent {

  initialCredit: number = 0;

  constructor(public dialogRef: MatDialogRef<AddAccountModalComponent>) {}

  onClose(): void {
    this.dialogRef.close(undefined);
  }

  onSave(): void {
    this.dialogRef.close(this.initialCredit);
  }


}
