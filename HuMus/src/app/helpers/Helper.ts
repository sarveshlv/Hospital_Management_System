import { FormControl, FormGroup } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';

export default class Helper {
  private static toast: NgToastService = new NgToastService(); // Static property to hold the NgToastService instance

  static validateAllFormFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((field) => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        Helper.validateAllFormFields(control);
      }
    });
  }
  static handleSuccess(message: string, summary: string) {
    if (Helper.toast) {
      Helper.toast.success({
        detail: message,
        summary: summary,
        duration: 3000,
      });
    } else {
      console.error('NgToastService not initialized.');
    }
    Helper.toast.success({
      detail: message,
      summary: summary,
      duration: 3000,
    });
  }
  static handleError(errorMessage: string, error: any) {
    if (Helper.toast) {
      Helper.toast.error({
        detail: errorMessage,
        summary: error.error,
        duration: 3000,
      });
    } else {
      console.error('NgToastService not initialized.');
    }
  }
}
