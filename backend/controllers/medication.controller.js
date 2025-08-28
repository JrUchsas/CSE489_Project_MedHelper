const Medication = require('../models/Medication');

// @route   POST api/medications
// @desc    Add a new medication
// @access  Private
exports.addMedication = async (req, res) => {
    const { name, dosage, schedule, stockCount, familyMemberId } = req.body;

    try {
        // Ensure familyMemberId is a valid ObjectId or null
        let parsedFamilyMemberId = null;
        if (familyMemberId) {
            // Check if it's a valid ObjectId string
            if (mongoose.Types.ObjectId.isValid(familyMemberId)) {
                parsedFamilyMemberId = familyMemberId;
            } else {
                // If it's not a valid ObjectId string, it's an error
                return res.status(400).json({ msg: 'Invalid familyMemberId format' });
            }
        }

        const newMedication = new Medication({
            userId: req.user.id,
            name,
            dosage,
            schedule,
            stockCount,
            familyMemberId: parsedFamilyMemberId // Use the parsed ID
        });

        const medication = await newMedication.save();
        res.json(medication);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};

// @route   GET api/medications
// @desc    Get all medications for a user (and their family)
// @access  Private
exports.getMedications = async (req, res) => {
    try {
        const medications = await Medication.find({ userId: req.user.id });
        res.json(medications);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};

// @route   PUT api/medications/:id
// @desc    Update a medication (e.g., decrement stock)
// @access  Private
exports.updateMedication = async (req, res) => {
    const { stockCount } = req.body;

    try {
        let medication = await Medication.findById(req.params.id);

        if (!medication) {
            return res.status(404).json({ msg: 'Medication not found' });
        }

        // Make sure user owns medication
        if (medication.userId.toString() !== req.user.id) {
            return res.status(401).json({ msg: 'Not authorized' });
        }

        medication = await Medication.findByIdAndUpdate(
            req.params.id,
            { $set: { stockCount } },
            { new: true }
        );

        res.json(medication);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};

// @route   DELETE api/medications/:id
// @desc    Delete a medication
// @access  Private
exports.deleteMedication = async (req, res) => {
    try {
        let medication = await Medication.findById(req.params.id);

        if (!medication) {
            return res.status(404).json({ msg: 'Medication not found' });
        }

        // Make sure user owns medication
        if (medication.userId.toString() !== req.user.id) {
            return res.status(401).json({ msg: 'Not authorized' });
        }

        await Medication.findByIdAndRemove(req.params.id);

        res.json({ msg: 'Medication removed' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};