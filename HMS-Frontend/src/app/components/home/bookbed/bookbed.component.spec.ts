import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookbedComponent } from './bookbed.component';

describe('BookbedComponent', () => {
  let component: BookbedComponent;
  let fixture: ComponentFixture<BookbedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BookbedComponent]
    });
    fixture = TestBed.createComponent(BookbedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
