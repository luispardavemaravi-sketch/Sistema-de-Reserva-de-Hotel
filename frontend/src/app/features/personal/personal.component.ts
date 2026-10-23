import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelAdminStore, ShiftConfig } from '../../core/demo/hotel-admin.store';
import { DemoEmployee, HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

@Component({
  selector: 'app-personal',
  imports: [ReactiveFormsModule, ModalFocusDirective],
  templateUrl: './personal.component.html',
  styles: [
    `
      .shift-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 14px;
        margin-top: 18px;
      }
      .shift-card h3 {
        font-size: 15px;
        margin: 0 0 5px;
      }
      .shift-card p {
        color: var(--muted, #60717a);
        font-size: 12px;
        margin: 0 0 14px;
      }
      .actions {
        display: flex;
        gap: 6px;
        flex-wrap: wrap;
      }
      .actions .btn {
        font-size: 11px;
        min-height: 29px;
        padding: 4px 8px;
      }
      .shift-card .actions {
        margin-top: 15px;
      }
      .error {
        color: #ad353b;
        font-size: 13px;
      }
      @media (max-width: 850px) {
        .shift-grid {
          grid-template-columns: 1fr 1fr;
        }
      }
      @media (max-width: 550px) {
        .shift-grid {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class PersonalComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly fb = inject(FormBuilder);
  readonly tabs = ['Empleados', 'Turnos'];
  readonly tab = signal('Empleados');
  readonly query = signal('');
  readonly dialog = signal<'employee' | 'shift' | 'deleteEmployee' | 'deleteShift' | null>(null);
  readonly editingEmployee = signal<number | null>(null);
  readonly editingShift = signal<number | null>(null);
  readonly error = signal('');
  readonly filtered = computed(() =>
    this.store
      .employees()
      .filter((row) =>
        `${row.name} ${row.role} ${row.shift}`.toLowerCase().includes(this.query().toLowerCase()),
      ),
  );
  readonly activeCount = computed(
    () => this.store.employees().filter((row) => row.status === 'Activo').length,
  );
  readonly employeeForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    document: [''],
    email: ['', Validators.email],
    phone: [''],
    role: ['Recepción', Validators.required],
    shift: ['', Validators.required],
    status: ['Activo'],
  });
  readonly shiftForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    start: ['07:00', Validators.required],
    end: ['15:00', Validators.required],
  });
  setQuery(event: Event) {
    this.query.set((event.target as HTMLInputElement).value);
  }
  shiftCount(name: string) {
    return this.store.employees().filter((row) => row.shift === name).length;
  }
  openEmployee(employee?: DemoEmployee) {
    this.editingEmployee.set(employee?.id || null);
    this.error.set('');
    this.employeeForm.setValue({
      name: employee?.name || '',
      document: employee?.document || '',
      email: employee?.email || '',
      phone: employee?.phone || '',
      role: employee?.role || 'Recepción',
      shift: employee?.shift || this.admin.shifts()[0]?.name || '',
      status: employee?.status || 'Activo',
    });
    this.dialog.set('employee');
  }
  saveEmployee() {
    if (this.employeeForm.invalid) {
      this.error.set('Ingrese nombre, cargo y turno válidos.');
      return;
    }
    const value = this.employeeForm.getRawValue();
    const id = this.editingEmployee();
    this.store.employees.update((rows) =>
      id === null
        ? [
            ...rows,
            {
              id: Math.max(0, ...rows.map((row) => row.id)) + 1,
              ...value,
              status: value.status as DemoEmployee['status'],
            },
          ]
        : rows.map((row) =>
            row.id === id
              ? { ...row, ...value, status: value.status as DemoEmployee['status'] }
              : row,
          ),
    );
    this.dialog.set(null);
  }
  toggleStatus(id: number) {
    this.store.employees.update((rows) =>
      rows.map((row) =>
        row.id === id ? { ...row, status: row.status === 'Activo' ? 'Descanso' : 'Activo' } : row,
      ),
    );
  }
  askDeleteEmployee(id: number) {
    this.editingEmployee.set(id);
    this.dialog.set('deleteEmployee');
  }
  deleteEmployee() {
    const id = this.editingEmployee();
    this.store.employees.update((rows) => rows.filter((row) => row.id !== id));
    this.dialog.set(null);
  }
  openShift(shift?: ShiftConfig) {
    this.editingShift.set(shift?.id || null);
    this.error.set('');
    this.shiftForm.setValue({
      name: shift?.name || '',
      start: shift?.start || '07:00',
      end: shift?.end || '15:00',
    });
    this.dialog.set('shift');
  }
  saveShift() {
    if (this.shiftForm.invalid) {
      this.error.set('Complete nombre y horario.');
      return;
    }
    const value = this.shiftForm.getRawValue();
    const id = this.editingShift();
    if (
      this.admin
        .shifts()
        .some((row) => row.name.toLowerCase() === value.name.toLowerCase() && row.id !== id)
    ) {
      this.error.set('Ese turno ya existe.');
      return;
    }
    const previous = this.admin.shifts().find((row) => row.id === id);
    this.admin.shifts.update((rows) =>
      id === null
        ? [...rows, { id: Math.max(0, ...rows.map((row) => row.id)) + 1, ...value }]
        : rows.map((row) => (row.id === id ? { ...row, ...value } : row)),
    );
    if (previous && previous.name !== value.name)
      this.store.employees.update((rows) =>
        rows.map((row) => (row.shift === previous.name ? { ...row, shift: value.name } : row)),
      );
    this.dialog.set(null);
  }
  askDeleteShift(id: number) {
    this.editingShift.set(id);
    this.error.set('');
    this.dialog.set('deleteShift');
  }
  deleteShift() {
    const id = this.editingShift();
    const shift = this.admin.shifts().find((row) => row.id === id);
    if (shift && this.shiftCount(shift.name)) {
      this.error.set('Reasigne a los empleados antes de eliminar el turno.');
      return;
    }
    this.admin.shifts.update((rows) => rows.filter((row) => row.id !== id));
    this.dialog.set(null);
  }
}
