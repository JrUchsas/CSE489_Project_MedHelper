const mongoose = require('mongoose');

const familyMemberSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    name: {
        type: String,
        required: true
    },
    relationship: {
        type: String,
        required: true
    },
    dateOfBirth: {
        type: Date,
        required: true
    },
    medications: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Medication'
    }]
}, { timestamps: true });

module.exports = mongoose.model('FamilyMember', familyMemberSchema);
