package com.example.terrabill.ui.customers

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.terrabill.R
import com.example.terrabill.data.model.Customer
import com.example.terrabill.databinding.ItemCustomerBinding

class CustomerAdapter(
    private val onUpdateCustomer: (Customer) -> Unit,
    private val onDeleteCustomer: (Customer) -> Unit
) : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding =
            ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding, onUpdateCustomer, onDeleteCustomer)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CustomerViewHolder(
        private val binding: ItemCustomerBinding,
        private val onUpdateCustomer: (Customer) -> Unit,
        private val onDeleteCustomer: (Customer) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.textCustomerName.text = listOfNotNull(
                "${customer.firstname} ${customer.lastname}".takeIf { it.isNotBlank() },
                customer.organization
            ).joinToString(" | ")

            binding.textCustomerPhone.text = customer.phone.orEmpty()
            binding.textCustomerAddress.text =
                "${customer.street} ${customer.houseNumber}, ${customer.postcode} ${customer.city}"

            binding.buttonMenu.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.menuInflater.inflate(R.menu.menu_customer_item, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_call -> {
                            val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${customer.phone ?: ""}")
                            }
                            view.context.startActivity(phoneIntent)
                            true
                        }

                        R.id.action_navigate -> {
                            val address =
                                "${customer.street} ${customer.houseNumber}, ${customer.postcode} ${customer.city}"
                            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            view.context.startActivity(mapIntent)
                            true
                        }

                        R.id.action_edit -> {
                            val context = view.context
                            val dialogView = LayoutInflater.from(context)
                                .inflate(R.layout.dialog_edit_customer, null)

                            val etFirstName = dialogView.findViewById<EditText>(R.id.etFirstName)
                            val etLastName = dialogView.findViewById<EditText>(R.id.etLastName)
                            val etOrganization =
                                dialogView.findViewById<EditText>(R.id.etOrganization)
                            val etStreet = dialogView.findViewById<EditText>(R.id.etStreet)
                            val etHouseNumber =
                                dialogView.findViewById<EditText>(R.id.etHouseNumber)
                            val etPostcode = dialogView.findViewById<EditText>(R.id.etPostcode)
                            val etCity = dialogView.findViewById<EditText>(R.id.etCity)
                            val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)

                            // Bestehende Werte einfügen
                            etFirstName.setText(customer.firstname)
                            etLastName.setText(customer.lastname)
                            etOrganization.setText(customer.organization ?: "")
                            etStreet.setText(customer.street)
                            etHouseNumber.setText(customer.houseNumber)
                            etPostcode.setText(customer.postcode)
                            etCity.setText(customer.city)
                            etPhone.setText(customer.phone ?: "")

                            AlertDialog.Builder(context)
                                .setTitle("Kunden bearbeiten")
                                .setView(dialogView)
                                .setPositiveButton("Speichern") { _, _ ->
                                    val updatedCustomer =
                                        etPhone.text.toString().ifBlank { null }?.let {
                                            customer.copy(
                                                firstname = etFirstName.text.toString(),
                                                lastname = etLastName.text.toString(),
                                                organization = etOrganization.text.toString()
                                                    .ifBlank { null },
                                                street = etStreet.text.toString(),
                                                houseNumber = etHouseNumber.text.toString(),
                                                postcode = etPostcode.text.toString(),
                                                city = etCity.text.toString(),
                                                phone = it
                                            )
                                        }
                                    if (updatedCustomer != null) {
                                        onUpdateCustomer(updatedCustomer)
                                    }
                                }
                                .setNegativeButton("Abbrechen", null)
                                .show()
                            true
                        }

                        R.id.action_delete -> {
                            AlertDialog.Builder(view.context)
                                .setTitle("Kunde löschen")
                                .setMessage("Möchten Sie diesen Kunden wirklich löschen?")
                                .setPositiveButton("Löschen") { _, _ ->
                                    onDeleteCustomer(customer)
                                }
                                .setNegativeButton("Abbrechen", null)
                                .show()
                            true
                        }

                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer) = oldItem == newItem
    }
}