import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CatalogItem, HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { DemoConsumption, HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';
import { AuthService } from '../../core/services/auth/auth.service';

@Component({
  selector: 'app-servicios',
  imports: [ReactiveFormsModule, ModalFocusDirective],
  templateUrl: './servicios.component.html',
  styles: [
    `
      .service-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 13px;
        margin-top: 18px;
      }
      .service-card {
        min-height: 140px;
      }
      .service-card h3 {
        font-size: 15px;
        margin: 0 0 4px;
      }
      .service-card p {
        font-size: 12px;
        color: var(--muted, #60717b);
        margin: 0 0 13px;
      }
      .service-card strong {
        font-size: 18px;
      }
      .service-card .actions {
        display: flex;
        gap: 6px;
        flex-wrap: wrap;
        margin-top: 12px;
      }
      .service-card .actions .btn {
        font-size: 11px;
        min-height: 29px;
      }
      .error {
        color: #ad353b;
        font-size: 13px;
      }
      .toolbar-actions {
        display: flex;
        gap: 8px;
      }
      .cancelled {
        opacity: 0.55;
        text-decoration: line-through;
      }
      @media (max-width: 900px) {
        .service-grid {
          grid-template-columns: repeat(2, 1fr);
        }
      }
      @media (max-width: 550px) {
        .service-grid {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class ServiciosComponent {
  readonly auth = inject(AuthService);
  readonly isAdmin = computed(() => this.auth.role() === 'ADMINISTRADOR');
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly fb = inject(FormBuilder);
  readonly tabs = ['Consumos', 'Catálogo', 'Áreas'];
  readonly activeTab = signal('Consumos');
  readonly query = signal('');
  readonly dialog = signal<'consumption' | 'catalog' | 'area' | 'void' | null>(null);
  readonly editingId = signal<number | null>(null);
  readonly editingArea = signal<string | null>(null);
  readonly selectedConsumption = signal<DemoConsumption | null>(null);
  readonly error = signal('');
  readonly total = computed(() =>
    this.store
      .consumptions()
      .filter((row) => !row.cancelled)
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly filtered = computed(() =>
    this.store
      .consumptions()
      .filter((row) =>
        `${row.room} ${row.service}`.toLowerCase().includes(this.query().toLowerCase()),
      ),
  );
  readonly catalogFiltered = computed(() =>
    this.admin
      .catalog()
      .filter((row) =>
        `${row.name} ${row.category} ${row.area}`
          .toLowerCase()
          .includes(this.query().toLowerCase()),
      ),
  );
  readonly consumptionForm = this.fb.nonNullable.group({
    room: ['', Validators.required],
    itemId: [0, [Validators.required, Validators.min(1)]],
    quantity: [1, [Validators.required, Validators.min(1)]],
    method: ['Efectivo'],
  });
  readonly catalogForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    category: ['Servicio'],
    area: ['Restaurante'],
    price: [0, [Validators.required, Validators.min(0)]],
    active: [true],
  });
  readonly areaForm = this.fb.nonNullable.group({ name: ['', Validators.required] });
  setQuery(event: Event) {
    this.query.set((event.target as HTMLInputElement).value);
  }
  openConsumption() {
    this.error.set('');
    this.consumptionForm.reset();
    this.consumptionForm.patchValue({ method: this.admin.paymentMethods()[0] || 'Efectivo' });
    this.dialog.set('consumption');
  }
  saveConsumption() {
    if (this.consumptionForm.invalid) {
      this.error.set('Seleccione habitación, producto y cantidad.');
      return;
    }
    const value = this.consumptionForm.getRawValue();
    const item = this.admin.catalog().find((row) => row.id === value.itemId);
    if (!item) return;
    const id = Math.max(0, ...this.store.consumptions().map((row) => row.id)) + 1;
    const amount = item.price * value.quantity;
    this.store.consumptions.update((rows) => [
      {
        id,
        room: value.room,
        service: item.name,
        quantity: value.quantity,
        amount,
        time: new Intl.DateTimeFormat('es-PE', { hour: '2-digit', minute: '2-digit' }).format(
          new Date(),
        ),
      },
      ...rows,
    ]);
    this.admin.addLedger({
      description: `${item.name} · habitación ${value.room}`,
      kind: 'Consumo',
      amount,
      method: value.method,
      room: value.room,
      sourceId: id,
    });
    this.dialog.set(null);
  }
  openCatalog(item?: CatalogItem) {
    if (!this.isAdmin()) return;
    this.editingId.set(item?.id || null);
    this.error.set('');
    this.catalogForm.setValue({
      name: item?.name || '',
      category: item?.category || 'Servicio',
      area: item?.area || this.admin.areas()[0] || '',
      price: item?.price || 0,
      active: item?.active ?? true,
    });
    this.dialog.set('catalog');
  }
  saveCatalog() {
    if (!this.isAdmin()) return;
    if (this.catalogForm.invalid) {
      this.error.set('Complete nombre y precio válido.');
      return;
    }
    const value = this.catalogForm.getRawValue();
    const id = this.editingId();
    this.admin.catalog.update((rows) =>
      id === null
        ? [...rows, { id: Math.max(0, ...rows.map((row) => row.id)) + 1, ...value }]
        : rows.map((row) => (row.id === id ? { ...row, ...value } : row)),
    );
    this.dialog.set(null);
  }
  deleteCatalog(id: number) {
    if (!this.isAdmin()) return;
    this.admin.catalog.update((rows) => rows.filter((row) => row.id !== id));
  }
  openArea(area?: string) {
    if (!this.isAdmin()) return;
    this.editingArea.set(area || null);
    this.areaForm.setValue({ name: area || '' });
    this.error.set('');
    this.dialog.set('area');
  }
  saveArea() {
    if (!this.isAdmin()) return;
    if (this.areaForm.invalid) return;
    const name = this.areaForm.getRawValue().name.trim();
    const old = this.editingArea();
    if (
      this.admin.areas().some((area) => area.toLowerCase() === name.toLowerCase() && area !== old)
    ) {
      this.error.set('El área ya existe.');
      return;
    }
    this.admin.areas.update((rows) =>
      old ? rows.map((area) => (area === old ? name : area)) : [...rows, name],
    );
    if (old)
      this.admin.catalog.update((rows) =>
        rows.map((item) => (item.area === old ? { ...item, area: name } : item)),
      );
    this.dialog.set(null);
  }
  deleteArea(area: string) {
    if (!this.isAdmin()) return;
    if (this.admin.catalog().some((item) => item.area === area)) {
      this.error.set('El área tiene productos o servicios asignados.');
      return;
    }
    this.admin.areas.update((rows) => rows.filter((item) => item !== area));
  }
  serviceCount(area: string) {
    return this.admin.catalog().filter((item) => item.area === area).length;
  }
  askVoid(row: DemoConsumption) {
    if (!this.isAdmin()) return;
    this.selectedConsumption.set(row);
    this.dialog.set('void');
  }
  voidConsumption() {
    if (!this.isAdmin()) return;
    const row = this.selectedConsumption();
    if (!row) return;
    this.store.consumptions.update((rows) =>
      rows.map((item) => (item.id === row.id ? { ...item, cancelled: true } : item)),
    );
    this.admin.removeLedgerSource(row.id);
    this.dialog.set(null);
  }
}
