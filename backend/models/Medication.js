const mongoose = require('mongoose');

const medicationSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    familyMemberId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'FamilyMember',
        required: false // Can be null if medication is for the user themselves
    },
    name: {
        type: String,
        required: true
    },
    dosage: {
        type: String,
        required: true
    },
    schedule: {
        type: String, // More flexible for various schedules, e.g., "daily", "weekly", specific times
        required: true
    },
    stockCount: {
        type: Number,
        required: false,
        default: 0
    },
    notes: {
        type: String,
        required: false
    }
}, { timestamps: true });

module.exports = mongoose.model('Medication', medicationSchema);
