const FamilyMember = require('../models/FamilyMember');

// @route   POST api/family
// @desc    Add a new family member
// @access  Private
exports.addFamilyMember = async (req, res) => {
    const { name, relationship, dateOfBirth } = req.body;

    try {
        const newFamilyMember = new FamilyMember({
            userId: req.user.id,
            name,
            relationship,
            dateOfBirth
        });

        const familyMember = await newFamilyMember.save();
        res.json(familyMember);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};

// @route   GET api/family
// @desc    Get all family members for a user
// @access  Private
exports.getFamilyMembers = async (req, res) => {
    try {
        const familyMembers = await FamilyMember.find({ userId: req.user.id });
        res.json(familyMembers);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};

// @route   DELETE api/family/:id
// @desc    Delete a family member
// @access  Private
exports.deleteFamilyMember = async (req, res) => {
    try {
        let familyMember = await FamilyMember.findById(req.params.id);

        if (!familyMember) {
            return res.status(404).json({ msg: 'Family member not found' });
        }

        // Make sure user owns family member
        if (familyMember.userId.toString() !== req.user.id) {
            return res.status(401).json({ msg: 'Not authorized' });
        }

        await FamilyMember.findByIdAndRemove(req.params.id);

        res.json({ msg: 'Family member removed' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
};