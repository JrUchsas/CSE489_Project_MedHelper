const mongoose = require('mongoose');

const medicationSchema = new mongoose.Schema({
    familyMemberId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'FamilyMember',
        required: true
    },
    name: {
        type: String,
        required: true
    },
    dosage: {
        type: String,
        required: true
    },
    frequency: {
        type: String,
        required: true
    },
    startDate: {
        type: Date,
        required: true
    },
    endDate: {
        type: Date,
        required: false
    },
    notes: {
        type: String,
        required: false
    }
}, { timestamps: true });

module.exports = mongoose.model('Medication', medicationSchema);
